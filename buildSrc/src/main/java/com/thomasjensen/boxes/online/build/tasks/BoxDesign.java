package com.thomasjensen.boxes.online.build.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * The JSON metadata about one box design.
 */
public class BoxDesign
{
    /** who configured the design for <i>boxes</i> */
    @JsonProperty
    private String author = null;

    /** who originally created the ASCII art */
    @JsonProperty
    private String designer = null;

    @JsonProperty
    private DefaultPadding defaultPadding = null;

    @JsonProperty
    private String sample = null;



    static class DefaultPadding
    {
        @JsonProperty
        private int top = 0;

        @JsonProperty
        private int right = 0;

        @JsonProperty
        private int bottom = 0;

        @JsonProperty
        private int left = 0;



        public int getTop()
        {
            return top;
        }



        public void setTop(final int pTop)
        {
            top = pTop;
        }



        public int getRight()
        {
            return right;
        }



        public void setRight(final int pRight)
        {
            right = pRight;
        }



        public int getBottom()
        {
            return bottom;
        }



        public void setBottom(final int pBottom)
        {
            bottom = pBottom;
        }



        public int getLeft()
        {
            return left;
        }



        public void setLeft(final int pLeft)
        {
            left = pLeft;
        }
    }



    public String getAuthor()
    {
        return author;
    }



    public void setAuthor(final String pAuthor)
    {
        author = pAuthor;
    }



    public String getDesigner()
    {
        return designer;
    }



    public void setDesigner(final String pDesigner)
    {
        designer = pDesigner;
    }



    public DefaultPadding getDefaultPadding()
    {
        return defaultPadding;
    }



    public void setDefaultPadding(final DefaultPadding pDefaultPadding)
    {
        defaultPadding = pDefaultPadding;
    }



    public String getSample()
    {
        return sample;
    }



    public void setSample(final String pSample)
    {
        sample = pSample;
    }
}
