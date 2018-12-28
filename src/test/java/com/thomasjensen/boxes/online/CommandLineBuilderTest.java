package com.thomasjensen.boxes.online;

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
