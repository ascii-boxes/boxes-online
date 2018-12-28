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

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;


/**
 * Some unit tests for the {@link CommandLineBuilder}.
 */
public class CommandLineBuilderTest
{
    @Test
    public void testVersionLeavesOutAotherArguments()
    {
        Invocation invocation = new Invocation();
        invocation.setVersion(true);
        Invocation.Size size = new Invocation.Size();
        size.setWidth(30);
        size.setHeight(11);
        invocation.setSize(size);

        final List<String> actual = new CommandLineBuilder().build(invocation);

        Assertions.assertThat(actual).isEqualTo(Lists.list("boxes", "-v"));
    }



    @Test
    public void testAllArguments()
    {
        Invocation invocation = new Invocation();
        Invocation.Alignment alignment = new Invocation.Alignment();
        alignment.setHorizontal(HorzAlign.Center);
        alignment.setVertical(VertAlign.Bottom);
        alignment.setJustification(HorzAlign.Left);
        invocation.setAlignment(alignment);
        invocation.setDesign("dog");
        Invocation.Padding padding = new Invocation.Padding();
        padding.setTop(1);
        padding.setRight(2);
        padding.setBottom(3);
        padding.setLeft(4);
        invocation.setPadding(padding);
        Invocation.Size size = new Invocation.Size();
        size.setWidth(30);
        size.setHeight(11);
        invocation.setSize(size);
        invocation.setTabDistance(4);

        final List<String> actual = new CommandLineBuilder().build(invocation);

        Assertions.assertThat(actual).isEqualTo(Lists.list(//
            "boxes", "-q", "-i", "text", "-a", "hcvbjl", "-d", "dog", "-p", "t1r2b3l4", "-s", "30x11", "-t", "4"));
    }
}
