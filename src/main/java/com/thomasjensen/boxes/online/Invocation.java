package com.thomasjensen.boxes.online;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;


/**
 * Describes an invocation of *boxes* with all the parameters.
 */
public class Invocation
{
    @JsonProperty
    private Alignment alignment;

    @JsonProperty
    private String design;

    @JsonProperty
    private IndentationMode indentationMode;

    @JsonProperty
    private boolean listDesigns;

    @JsonProperty
    private Padding padding;

    @JsonProperty
    private Size size;

    @JsonProperty
    private Tabs tabHandling;

    @JsonProperty
    private boolean version;



    public static class Alignment
    {
        public static final Alignment DEFAULT = defaultAlignment();

        @Nullable
        @JsonProperty
        private HorzAlign horizontal;

        @Nullable
        @JsonProperty
        private VertAlign vertical;

        @Nullable
        @JsonProperty
        private HorzAlign justification;



        private static Alignment defaultAlignment()
        {
            Alignment result = new Alignment();
            result.setHorizontal(HorzAlign.Left);
            result.setVertical(VertAlign.Top);
            result.setJustification(null);
            return result;
        }



        @Nullable
        public HorzAlign getHorizontal()
        {
            return horizontal;
        }



        public void setHorizontal(@Nullable final HorzAlign pHorizontal)
        {
            horizontal = pHorizontal;
        }



        @Nullable
        public VertAlign getVertical()
        {
            return vertical;
        }



        public void setVertical(@Nullable final VertAlign pVertical)
        {
            vertical = pVertical;
        }



        @Nullable
        public HorzAlign getJustification()
        {
            return justification;
        }



        public void setJustification(@Nullable final HorzAlign pJustification)
        {
            justification = pJustification;
        }
    }



    public static class Padding
    {
        public static final Padding DEFAULT = new Padding();

        @JsonProperty
        private int top;

        @JsonProperty
        private int right;

        @JsonProperty
        private int bottom;

        @JsonProperty
        private int left;



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



    public static class Size
    {
        @JsonProperty
        private int width;

        @JsonProperty
        private int height;



        public int getWidth()
        {
            return width;
        }



        public void setWidth(final int pWidth)
        {
            width = pWidth;
        }



        public int getHeight()
        {
            return height;
        }



        public void setHeight(final int pHeight)
        {
            height = pHeight;
        }
    }



    public static class Tabs
    {
        @JsonProperty
        private int distance;

        @JsonProperty
        private LeadingTabHandlingMode leadingTabs;



        public int getDistance()
        {
            return distance;
        }



        public void setDistance(final int pDistance)
        {
            distance = pDistance;
        }



        @Nullable
        public LeadingTabHandlingMode getLeadingTabs()
        {
            return leadingTabs;
        }



        public void setLeadingTabs(@Nullable final LeadingTabHandlingMode pLeadingTabs)
        {
            leadingTabs = pLeadingTabs;
        }
    }



    @Nullable
    public Alignment getAlignment()
    {
        return alignment;
    }



    public void setAlignment(@Nullable final Alignment pAlignment)
    {
        alignment = pAlignment;
    }



    @Nullable
    public String getDesign()
    {
        return design;
    }



    public void setDesign(@Nullable final String pDesign)
    {
        design = pDesign;
    }



    @Nullable
    public IndentationMode getIndentationMode()
    {
        return indentationMode;
    }



    public void setIndentationMode(@Nullable final IndentationMode pIndentationMode)
    {
        indentationMode = pIndentationMode;
    }



    public boolean isListDesigns()
    {
        return listDesigns;
    }



    public void setListDesigns(final boolean pListDesigns)
    {
        listDesigns = pListDesigns;
    }



    @Nullable
    public Padding getPadding()
    {
        return padding;
    }



    public void setPadding(@Nullable final Padding pPadding)
    {
        padding = pPadding;
    }



    @Nullable
    public Size getSize()
    {
        return size;
    }



    public void setSize(@Nullable final Size pSize)
    {
        size = pSize;
    }



    @Nullable
    public Tabs getTabHandling()
    {
        return tabHandling;
    }



    public void setTabHandling(@Nullable final Tabs pTabHandling)
    {
        tabHandling = pTabHandling;
    }



    public boolean isVersion()
    {
        return version;
    }



    public void setVersion(final boolean pVersion)
    {
        version = pVersion;
    }
}
