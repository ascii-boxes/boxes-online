package com.thomasjensen.boxes.online;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.Assert;
import org.junit.Test;


public class InvocationTest
{
    @Test
    public void testDeserializationFull()
        throws IOException
    {
        final String json = "{\"alignment\": {\"horizontal\": \"center\", \"vertical\": \"center\"}, "
            + "\"design\": \"parchment\", \"indentationMode\": \"box\", \"listDesigns\": false,"
            + "\"padding\": {\"top\": 1, \"right\": 2, \"bottom\": 1, \"left\": 2}, "
            + "\"size\": {\"width\": 42, \"height\": 10}, "
            + "\"tabHandling\": {\"distance\": 4, \"leadingTabs\": \"expand\"}, \"version\": false}";
        final Invocation underTest = new ObjectMapper().readValue(json, Invocation.class);

        Assert.assertNotNull(underTest);
        Assert.assertNotNull(underTest.getAlignment());
        Assert.assertEquals(HorzAlign.Center, underTest.getAlignment().getHorizontal());
        Assert.assertEquals(VertAlign.Center, underTest.getAlignment().getVertical());
        Assert.assertNull(underTest.getAlignment().getJustification());
        Assert.assertEquals("parchment", underTest.getDesign());
        Assert.assertEquals(IndentationMode.Box, underTest.getIndentationMode());
        Assert.assertFalse(underTest.isListDesigns());
        Assert.assertNotNull(underTest.getSize());
        Assert.assertEquals(42, underTest.getSize().getWidth());
        Assert.assertEquals(10, underTest.getSize().getHeight());
        Assert.assertNotNull(underTest.getTabHandling());
        Assert.assertEquals(4, underTest.getTabHandling().getDistance());
        Assert.assertEquals(LeadingTabHandlingMode.Expand, underTest.getTabHandling().getLeadingTabs());
        Assert.assertFalse(underTest.isVersion());
    }



    @Test
    public void testDeserializationVersionOnly()
        throws IOException
    {
        final String json = "{\"version\": true}";
        final Invocation underTest = new ObjectMapper().readValue(json, Invocation.class);

        Assert.assertNotNull(underTest);
        Assert.assertNull(underTest.getAlignment());
        Assert.assertNull(underTest.getDesign());
        Assert.assertNull(underTest.getIndentationMode());
        Assert.assertFalse(underTest.isListDesigns());
        Assert.assertNull(underTest.getSize());
        Assert.assertNull(underTest.getTabHandling());
        Assert.assertTrue(underTest.isVersion());
    }



    @Test
    public void testDefaultAlignment()
    {
        final Invocation.Alignment underTest = Invocation.Alignment.DEFAULT;
        Assert.assertEquals(HorzAlign.Left, underTest.getHorizontal());
        Assert.assertEquals(VertAlign.Top, underTest.getVertical());
        Assert.assertNull(underTest.getJustification());
    }



    @Test
    public void testDefaultPadding()
    {
        final Invocation.Padding underTest = Invocation.Padding.DEFAULT;
        Assert.assertEquals(0, underTest.getTop());
        Assert.assertEquals(0, underTest.getRight());
        Assert.assertEquals(0, underTest.getBottom());
        Assert.assertEquals(0, underTest.getLeft());
    }



    @Test(expected = InvalidFormatException.class)
    public void testInvalidJson()
        throws IOException
    {
        final String json = "{\"indentationMode\": \"INVALID\"}";
        new ObjectMapper().readValue(json, Invocation.class);
        Assert.fail("expected InvalidFormatException was not thrown");
    }



    @Test
    public void testSerialization()
        throws IOException
    {
        final Invocation original = new Invocation();
        Invocation.Alignment alignment = new Invocation.Alignment();
        alignment.setHorizontal(HorzAlign.Center);
        alignment.setVertical(VertAlign.Bottom);
        alignment.setJustification(HorzAlign.Left);
        original.setAlignment(alignment);
        original.setDesign("dog");
        original.setIndentationMode(IndentationMode.Text);
        original.setListDesigns(false);
        Invocation.Padding padding = new Invocation.Padding();
        padding.setTop(1);
        padding.setRight(2);
        padding.setBottom(3);
        padding.setLeft(4);
        original.setPadding(padding);
        Invocation.Size size = new Invocation.Size();
        size.setHeight(10);
        size.setWidth(42);
        original.setSize(size);
        Invocation.Tabs tabs = new Invocation.Tabs();
        tabs.setDistance(13);
        tabs.setLeadingTabs(LeadingTabHandlingMode.Unexpand);
        original.setTabHandling(tabs);
        original.setVersion(true);

        final String serialized = new ObjectMapper().writeValueAsString(original);
        final Invocation deserialized = new ObjectMapper().readValue(serialized, Invocation.class);

        Assert.assertNotNull(deserialized);
    }
}
