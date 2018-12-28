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

    // TODO HERE some more tests
}
