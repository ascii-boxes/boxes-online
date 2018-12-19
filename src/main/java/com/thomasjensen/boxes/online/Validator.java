package com.thomasjensen.boxes.online;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.SerializationUtils;


/**
 * Validates and optionally fixes an {@link Invocation}.
 */
public class Validator
{
    /** min. width that can be specified on the <i>boxes</i> command line */
    private static final int MIN_WIDTH = 1;

    /** max. characters per line, from {@code boxes.h.in} */
    private static final int MAX_WIDTH = 2048;

    /** min. height that can be specified on the <i>boxes</i> command line */
    private static final int MIN_HEIGHT = 1;

    /** max. number of input lines. Ten times that works too, but this limit constrains CPU and network time. */
    private static final int MAX_HEIGHT = 10000;

    /** min. tabstop distance that can be set on the <i>boxes</i> command line */
    private static final int MIN_TABSIZE = 1;

    /** max. tab stop distance, from {@code boxes.h.in} */
    private static final int MAX_TABSIZE = 16;

    private Invocation invocation;



    public Validator(@NonNull final Invocation pInvocation)
    {
        Assert.notNull(pInvocation, "Argument pInvocation is null");
        invocation = pInvocation;
    }



    public void validate()
        throws InvalidInvocationException
    {
        execute(invocation, true);
    }



    public Invocation checkup()
    {
        Invocation copyInvocation = (Invocation) SerializationUtils.deserialize(
            SerializationUtils.serialize(invocation));
        assert copyInvocation != null;
        try {
            return execute(copyInvocation, false);
        }
        catch (InvalidInvocationException e) {
            throw new IllegalStateException("bug in execute()", e);
        }
    }



    private Invocation execute(@NonNull final Invocation pInvocation, final boolean pThrowEx)
        throws InvalidInvocationException
    {
        handleBoxSize(pInvocation.getSize(), pThrowEx);
        handlePadding(pInvocation.getPadding());
        handleTabs(pInvocation.getTabHandling(), pThrowEx);
        return pInvocation;
    }



    private void handleBoxSize(@Nullable final Invocation.Size pSize, final boolean pThrowEx)
        throws InvalidInvocationException
    {
        if (pSize != null) {
            checkAndCorrect(pSize.getWidth() < MIN_WIDTH, () -> pSize.setWidth(MIN_WIDTH));
            if (pSize.getWidth() > MAX_WIDTH) {
                handleViolation(pThrowEx, () -> pSize.setWidth(MAX_WIDTH),
                    "Box width of " + pSize.getWidth() + " exceeds maximum width of " + MAX_WIDTH);
            }

            checkAndCorrect(pSize.getHeight() < MIN_HEIGHT, () -> pSize.setHeight(MIN_HEIGHT));
            if (pSize.getHeight() > MAX_HEIGHT) {
                handleViolation(pThrowEx, () -> pSize.setHeight(MAX_HEIGHT),
                    "Box height of " + pSize.getHeight() + " exceeds maximum height of " + MAX_HEIGHT);
            }
        }
    }



    private void handlePadding(@Nullable final Invocation.Padding pPadding)
        throws InvalidInvocationException
    {
        if (pPadding != null) {
            checkAndCorrect(pPadding.getTop() < 0, () -> pPadding.setTop(0));
            checkAndCorrect(pPadding.getRight() < 0, () -> pPadding.setRight(0));
            checkAndCorrect(pPadding.getBottom() < 0, () -> pPadding.setBottom(0));
            checkAndCorrect(pPadding.getLeft() < 0, () -> pPadding.setLeft(0));
        }
    }



    private void handleTabs(@Nullable final Invocation.Tabs pTabs, final boolean pThrowEx)
        throws InvalidInvocationException
    {
        if (pTabs != null) {
            checkAndCorrect(pTabs.getDistance() < MIN_TABSIZE, () -> pTabs.setDistance(MIN_TABSIZE));
            if (pTabs.getDistance() > MAX_TABSIZE) {
                handleViolation(pThrowEx, () -> pTabs.setDistance(MAX_TABSIZE),
                    "Tab distance of " + pTabs.getDistance() + " exceeds maximum tab distance of " + MAX_TABSIZE);
            }
        }
    }



    private void checkAndCorrect(final boolean pCondition, final Runnable pCompensation)
        throws InvalidInvocationException
    {
        if (pCondition) {
            handleViolation(false, pCompensation, null);
        }
    }



    private void handleViolation(final boolean pThrowEx, final Runnable pCompensation, final String pExMessage)
        throws InvalidInvocationException
    {
        if (pThrowEx) {
            throw new InvalidInvocationException(pExMessage);
        }
        pCompensation.run();
    }
}
