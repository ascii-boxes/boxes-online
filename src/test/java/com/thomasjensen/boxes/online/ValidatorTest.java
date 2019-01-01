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

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for {@link Validator}.
 */
public class ValidatorTest
{
    @Test(expected = InvalidInvocationException.class)
    public void testTabsizeTooLarge()
        throws InvalidInvocationException
    {
        Invocation invocation = new Invocation();
        invocation.setTabDistance(100);
        invocation.setContent("some content");
        Validator underTest = new Validator(invocation);
        underTest.validate();   // should fail because tab size too large
        Assert.fail("Expected InvalidInvocationException was not thrown");
    }



    @Test(expected = InvalidInvocationException.class)
    public void testNoContent()
        throws InvalidInvocationException
    {
        Invocation invocation = new Invocation();
        Validator underTest = new Validator(invocation);
        underTest.validate();   // should fail because no content was specified
        Assert.fail("Expected InvalidInvocationException was not thrown");
    }
}
