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
            + "\"design\": \"parchment\", \"padding\": {\"top\": 1, \"right\": 2, \"bottom\": 1, \"left\": 2}, "
            + "\"size\": {\"width\": 42, \"height\": 10}, \"tabDistance\": 4, \"version\": false}";
        final Invocation underTest = new ObjectMapper().readValue(json, Invocation.class);

        Assert.assertNotNull(underTest);
        Assert.assertNotNull(underTest.getAlignment());
        Assert.assertEquals(HorzAlign.Center, underTest.getAlignment().getHorizontal());
        Assert.assertEquals(VertAlign.Center, underTest.getAlignment().getVertical());
        Assert.assertEquals(HorzAlign.Left, underTest.getAlignment().getJustification());
        Assert.assertEquals("parchment", underTest.getDesign());
        Assert.assertNotNull(underTest.getSize());
        Assert.assertEquals(42, underTest.getSize().getWidth());
        Assert.assertEquals(10, underTest.getSize().getHeight());
        Assert.assertEquals(4, underTest.getTabDistance());
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
        Assert.assertNull(underTest.getSize());
        Assert.assertEquals(8, underTest.getTabDistance());
        Assert.assertTrue(underTest.isVersion());
    }



    @Test(expected = InvalidFormatException.class)
    public void testInvalidJson()
        throws IOException
    {
        final String json = "{\"alignment\": {\"horizontal\": \"INVALID\"}}";
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
        original.setTabDistance(4);
        original.setVersion(true);

        final String serialized = new ObjectMapper().writeValueAsString(original);
        final Invocation deserialized = new ObjectMapper().readValue(serialized, Invocation.class);

        Assert.assertNotNull(deserialized);
    }
}
