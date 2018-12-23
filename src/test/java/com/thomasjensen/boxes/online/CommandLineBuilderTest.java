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
        size.setHeight(11);
        size.setWidth(30);
        invocation.setSize(size);

        final List<String> actual = new CommandLineBuilder().build(invocation);

        Assertions.assertThat(actual).isEqualTo(Lists.list("boxes", "-v"));
    }



    @Test
    public void testListAllDesigns()
    {
        Invocation invocation = new Invocation();
        invocation.setListDesigns(true);
        Invocation.Size size = new Invocation.Size();
        size.setHeight(11);
        size.setWidth(30);
        invocation.setSize(size);

        final List<String> actual = new CommandLineBuilder().build(invocation);

        Assertions.assertThat(actual).isEqualTo(Lists.list("boxes", "-l"));
    }



    @Test
    public void testListSpecificDesign()
    {
        Invocation invocation = new Invocation();
        invocation.setListDesigns(true);
        invocation.setDesign("designName");
        Invocation.Size size = new Invocation.Size();
        size.setHeight(11);
        size.setWidth(30);
        invocation.setSize(size);

        final List<String> actual = new CommandLineBuilder().build(invocation);

        Assertions.assertThat(actual).isEqualTo(Lists.list("boxes", "-l", "-d", "designName"));
    }


    // TODO HERE some more tests
}
