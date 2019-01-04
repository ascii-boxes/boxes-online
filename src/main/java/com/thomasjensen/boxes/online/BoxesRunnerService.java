package com.thomasjensen.boxes.online;
/*
 * boxes-online - A Web UI for the 'boxes' tool
 * Copyright (C) 2018  Thomas Jensen and the contributors
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License, version 2, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.zeroturnaround.exec.InvalidResultException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stop.ProcessStopper;


/**
 * Perform one invocation of the <i>boxes</i> executable via a thread pool.
 */
@Service
public class BoxesRunnerService
{
    private static final Logger LOG = LoggerFactory.getLogger(BoxesRunnerService.class);

    /** how long a <i>boxes</i> execution may take at most */
    private static final long EXEC_TIMEOUT_SECS = 5L;

    /** how long a thread can wait for the <i>boxes</i> execution to finish, including queue time */
    private static final long QUEUE_TIMEOUT_SECS = 20L;



    /**
     * Forcibly stop a <i>boxes</i> worker process and log the fact.
     */
    private static class Stopper
        implements ProcessStopper
    {
        private static final Logger LOG = LoggerFactory.getLogger(Stopper.class);



        @Override
        public void stop(@NonNull final Process pProcess)
        {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Worker hung, trying to stop. Worker PID: " + pProcess.pid());
            }
            pProcess.destroyForcibly();
        }
    }



    // TODO How to shut down gracefully?
    private final ExecutorService executorService;



    public BoxesRunnerService(@Value("${boxes.executable.parallelism}") final int pNumWorkers,
        @NonNull final NamedThreadFactory pThreadFactory)
    {
        Assert.notNull(pThreadFactory, "required parameter pThreadFactory was not injected");
        Assert.isTrue(pNumWorkers > 0, "at least one worker must be configured");
        executorService = Executors.newFixedThreadPool(pNumWorkers, pThreadFactory);
        if (LOG.isInfoEnabled()) {
            LOG.info(getClass().getSimpleName() + " started with parallelism of " + pNumWorkers);
        }
    }



    public String execute(@NonNull final List<String> pCmdLine, @NonNull final String pInputText)
        throws InterruptedException, TimeoutException
    {
        Future<byte[]> future = executorService.submit(() -> {
            final ByteArrayInputStream bais = new ByteArrayInputStream(pInputText.getBytes(StandardCharsets.US_ASCII));
            if (LOG.isDebugEnabled()) {
                LOG.debug("Executing " + String.join(" ", pCmdLine));
            }
            return StopWatch.timeAndLog("Boxes execution", () ->//
                new ProcessExecutor().command(pCmdLine)//
                    .redirectInput(bais)//
                    .readOutput(true)//
                    .stopper(new Stopper()).timeout(EXEC_TIMEOUT_SECS, TimeUnit.SECONDS)//
                    .exitValueNormal()//
                    .execute()//
                    .getOutput().getBytes());
        });

        try {
            final byte[] bytes = future.get(QUEUE_TIMEOUT_SECS, TimeUnit.SECONDS);
            String output = new String(bytes, StandardCharsets.US_ASCII);
            if (!output.isBlank()) {
                LOG.debug("Boxes execution successful");
            }
            return output;
        }
        catch (TimeoutException e) {
            LOG.warn("Boxes execution timed out because no result was received after waiting for " + QUEUE_TIMEOUT_SECS
                + " seconds. Boxes may still have been started, but too late. " + e.getCause().getMessage());
            throw e;
        }
        catch (ExecutionException e) {
            if (e.getCause() instanceof InvalidResultException || e.getCause() instanceof IOException//
                || e.getCause() instanceof IllegalStateException)
            {
                // These are identified from org.zeroturnaround.exec.ProcessExecutor.waitFor() source
                throw new BoxesExecutionException(e.getCause());
            }
            else if (e.getCause() instanceof TimeoutException) {
                throw new BoxesExecutionException("Boxes executable ran for more than " + EXEC_TIMEOUT_SECS
                    + " seconds, which is why the call timed out", e.getCause());
            }
            throw new BoxesExecutionException("Something unexpected went wrong running Boxes", e);
        }
    }
}
