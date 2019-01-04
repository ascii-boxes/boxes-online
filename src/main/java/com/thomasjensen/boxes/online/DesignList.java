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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;


/**
 * Provides the list of supported box designs generated during build to the application.
 */
@Component
public class DesignList
{
    /** the name of the generated JSON file, keep in sync with <code>DesignListTask#outFile</code> */
    private static final String DESIGN_LIST_FILENAME = "box-designs.json";

    private static final Map<String, BoxDesign> DESIGN_LIST = readDesignList();



    private static Map<String, BoxDesign> readDesignList()
    {
        try (
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DESIGN_LIST_FILENAME);
            BufferedInputStream bis = new BufferedInputStream(Objects.requireNonNull(is)))
        {
            Map<String, BoxDesign> m = new ObjectMapper().readValue(bis, new TypeReference<Map<String, BoxDesign>>(){});
            if (m == null || m.isEmpty()) {
                throw new IllegalStateException("list of box designs is empty");
            }
            return m;
        }
        catch (NullPointerException e) {
            throw new IllegalStateException("file not found: " + DESIGN_LIST_FILENAME, e);
        }
        catch (IOException | RuntimeException e) {
            throw new IllegalStateException("error reading list of box designs", e);
        }
    }



    public boolean isSupported(@Nullable final String pDesignName)
    {
        boolean result = false;
        if (pDesignName != null) {
            result = DESIGN_LIST.containsKey(pDesignName);
        }
        return result;
    }
}
