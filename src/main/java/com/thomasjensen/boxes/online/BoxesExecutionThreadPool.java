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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


@Component
public class BoxesExecutionThreadPool
    extends ThreadPoolTaskExecutor
{
    private static final Logger LOG = LoggerFactory.getLogger(BoxesExecutionThreadPool.class);



    public BoxesExecutionThreadPool(@Value("${boxes.executable.parallelism}") final int pNumWorkers)
    {
        super();
        Assert.isTrue(pNumWorkers > 0, "at least one worker must be configured");

        setThreadFactory(new NamedThreadFactory());
        setCorePoolSize(pNumWorkers);
        setMaxPoolSize(pNumWorkers);
        initialize();

        if (LOG.isInfoEnabled()) {
            LOG.info(getClass().getSimpleName() + " started with parallelism of " + pNumWorkers);
        }
    }
}
