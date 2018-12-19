package com.thomasjensen.boxes.online;

/**
 * An {@link Invocation} contained invalid values.
 */
public class InvalidInvocationException
    extends Exception
{
    public InvalidInvocationException(final String pMessage)
    {
        super(pMessage);
    }
}
