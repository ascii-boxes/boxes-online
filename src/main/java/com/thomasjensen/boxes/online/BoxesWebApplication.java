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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * The main application class for this Spring Boot application. The action starts here.
 */
@SpringBootApplication
public class BoxesWebApplication
{
    public static void main(final String[] pArgs)
    {
        SpringApplication.run(BoxesWebApplication.class, pArgs);
    }
}
