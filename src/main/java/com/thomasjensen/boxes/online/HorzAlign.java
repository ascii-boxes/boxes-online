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

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Specify horizontal alignment.
 */
public enum HorzAlign
    implements ArgumentValue
{
    @JsonProperty("left")
    Left('l'),

    @JsonProperty("center")
    Center('c'),

    @JsonProperty("right")
    Right('r');


    private final char shortForm;



    private HorzAlign(final char pShortForm)
    {
        shortForm = pShortForm;
    }



    public char getShortForm()
    {
        return shortForm;
    }
}
