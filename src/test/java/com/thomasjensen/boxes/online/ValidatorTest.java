package com.thomasjensen.boxes.online;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for {@link Validator}.
 */
public class ValidatorTest
{
    @Test
    public void testVersion()
        throws InvalidInvocationException
    {
        Invocation invocation = new Invocation();
        invocation.setVersion(true);
        Validator underTest = new Validator(invocation);
        underTest.validate();
    }



    @Test(expected = InvalidInvocationException.class)
    public void testTabsizeTooLarge()
        throws InvalidInvocationException
    {
        Invocation invocation = new Invocation();
        invocation.setTabDistance(100);
        Validator underTest = new Validator(invocation);
        underTest.validate();
        Assert.fail("Expected InvalidInvocationException was not thrown");
    }
}
