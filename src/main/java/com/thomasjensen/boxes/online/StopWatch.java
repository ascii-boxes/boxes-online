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

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;


/**
 * Execute an action and log a message about how long it took.
 */
public final class StopWatch
{
    private static final Logger LOG = LoggerFactory.getLogger(StopWatch.class);



    /**
     * The action to time, with more precise exception declarations than {@code Callable}.
     *
     * @param <R> the type of the call result
     */
    @FunctionalInterface
    public interface TimedAction<R>
    {
        R run()
            throws IOException, InterruptedException, TimeoutException, ExecutionException;
    }



    private StopWatch()
    {
        super();
    }



    public static <R> R timeAndLog(@NonNull final String pDescription, @NonNull final TimedAction<R> pAction)
        throws IOException, InterruptedException, ExecutionException, TimeoutException
    {
        final long timeStart = System.nanoTime();
        try {
            return pAction.run();
        }
        finally {
            final long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - timeStart);
            if (LOG.isInfoEnabled()) {
                LOG.info("StopWatch: " + pDescription + " finished after " + elapsedMillis + " ms");
            }
        }
    }
}
