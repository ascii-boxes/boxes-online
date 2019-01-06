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

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;


/**
 * Thread factory for our boxes execution worker threads.
 */
public class NamedThreadFactory
    implements ThreadFactory
{
    private static final Logger LOG = LoggerFactory.getLogger(NamedThreadFactory.class);

    private static final Thread.UncaughtExceptionHandler UEH = (final Thread pThread, final Throwable pExc) -> {
        LOG.error("Uncaught exception in Boxes worker thread '" + pThread.getName() + "': " + pExc.getMessage(), pExc);
    };

    private final ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();



    @Override
    @SuppressWarnings("PMD.DoNotUseThreads")
    public Thread newThread(@NonNull final Runnable pRunnable)
    {
        Thread result = defaultThreadFactory.newThread(pRunnable);
        result.setDaemon(true);
        result.setName("boxes-exec-" + result.getName());
        result.setUncaughtExceptionHandler(UEH);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Created new thread " + result.getName());
        }
        return result;
    }
}

