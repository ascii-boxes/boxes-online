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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;


/**
 * Construct a complete <i>boxes</i> command line from a JSON {@link Invocation}.
 */
public class CommandLineBuilder
{
    /** <code>true</code> if we are currently running on Windows */
    private static final boolean OS_WIN = System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("windows");

    /** path to the <i>boxes</i> executable */
    static final String BOXES_EXECUTABLE = "boxes/boxes" + (OS_WIN ? ".exe" : "");

    /** <i>boxes</i> config file to use */
    private static final String BOXES_CONFIG = "boxes/boxes.cfg";

    private final Invocation invocation;

    private final List<String> cmdLine;



    public CommandLineBuilder(@NonNull final Invocation pInvocation)
    {
        Assert.notNull(pInvocation, "missing required argument: pInvocation");
        invocation = pInvocation;

        cmdLine = new ArrayList<>();
        cmdLine.add(BOXES_EXECUTABLE);
        cmdLine.add("-f");
        cmdLine.add(BOXES_CONFIG);
        cmdLine.add("-q");
        cmdLine.add("-i");
        cmdLine.add("text");
    }



    @NonNull
    public List<String> build()
    {
        alignment(invocation.getAlignment());
        design(invocation.getDesign());
        padding(invocation.getPadding());
        boxSize(invocation.getSize());

        cmdLine.add("-t");
        cmdLine.add(String.valueOf(invocation.getTabDistance()));

        return cmdLine;
    }



    private void alignment(@Nullable final Invocation.Alignment pAlignment)
    {
        StringBuilder sb = new StringBuilder();
        if (pAlignment != null) {
            alignmentFragment(sb, 'h', pAlignment.getHorizontal());
            alignmentFragment(sb, 'v', pAlignment.getVertical());
            alignmentFragment(sb, 'j', pAlignment.getJustification());
        }
        if (sb.length() > 0) {
            cmdLine.add("-a");
            cmdLine.add(sb.toString());
        }
    }



    private void alignmentFragment(@NonNull final StringBuilder pSb, final char pCode,
        @Nullable final ArgumentValue pArgumentValue)
    {
        if (pArgumentValue != null) {
            pSb.append(pCode);
            pSb.append(pArgumentValue.getShortForm());
        }
    }



    private void design(@Nullable final String pDesign)
    {
        if (pDesign != null) {
            cmdLine.add("-d");
            cmdLine.add(pDesign);
        }
    }



    private void padding(@Nullable final Invocation.Padding pPadding)
    {
        StringBuilder sb = new StringBuilder();
        if (pPadding != null) {
            paddingFragment(sb, 't', pPadding.getTop());
            paddingFragment(sb, 'r', pPadding.getRight());
            paddingFragment(sb, 'b', pPadding.getBottom());
            paddingFragment(sb, 'l', pPadding.getLeft());
        }
        if (sb.length() > 0) {
            cmdLine.add("-p");
            cmdLine.add(sb.toString());
        }
    }



    private void paddingFragment(@NonNull final StringBuilder pSb, final char pCode, final int pValue)
    {
        if (pValue > Invocation.Padding.NOT_SET) {
            pSb.append(pCode);
            pSb.append(pValue);
        }
    }



    private void boxSize(@Nullable final Invocation.Size pSize)
    {
        if (pSize != null) {
            cmdLine.add("-s");
            cmdLine.add(pSize.getWidth() + "x" + pSize.getHeight());
        }
    }
}
