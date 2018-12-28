package com.thomasjensen.boxes.online;
/*
 * boxes-online - A Web UI for the 'boxes' tool
 * Copyright (C) 2018  Thomas Jensen and the contributors
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stop.ProcessStopper;


/**
 * Perform one invocation of the <i>boxes</i> executable.
 */
public class BoxesRunner
    implements Callable<Void>
{
    private final String inputText;



    private static class Stopper
        implements ProcessStopper
    {
        @Override
        public void stop(final Process pProcess)
        {
            // TODO log System.err.println("Worker hung, trying to stop");
            pProcess.destroyForcibly();
        }
    }



    public BoxesRunner(final String pInputText)
    {
        inputText = pInputText;
    }



    @Override
    public Void call()
        throws Exception
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(inputText.getBytes(StandardCharsets.US_ASCII));

        try {
            byte[] bytes = new ProcessExecutor().command("boxes", "-d", "dog"/*, "-s", "2000x100000"*/)//
                .redirectInput(bais)//
                .readOutput(true)//
                .stopper(new Stopper()).timeout(5, TimeUnit.SECONDS)//
                .exitValueNormal()//
                .execute()//
                .getOutput().getBytes();
            String output = new String(bytes, StandardCharsets.US_ASCII);

            System.out.println(output);
        }
        catch (TimeoutException e) {
            System.out.println("Timed out, worker killed.");
        }
        // TODO implement call()
        return null;
    }
}
