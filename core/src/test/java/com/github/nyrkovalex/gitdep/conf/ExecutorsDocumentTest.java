package com.github.nyrkovalex.gitdep.conf;

import com.github.nyrkovalex.gitdep.UnitTest;
import org.intellij.lang.annotations.Language;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashSet;

public class ExecutorsDocumentTest extends UnitTest {

    @Language("XML") public static final String CONFIG_XML = "" +
            "<executors>\n" +
            "    <executor>first.Executor</executor>\n" +
            "    <executor>second.Executor</executor>\n" +
            "</executors>";

    @Test
    public void testShouldReadExecutorConfig() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(CONFIG_XML.getBytes());
        HashSet<String> expected = new HashSet<>(Arrays.asList("first.Executor", "second.Executor"));

        ExecutorsDocument ec = ExecutorsDocument.from(in);

        assertThat(ec.executors(), is(expected));
    }

    @Test(expected = MalformedConfigurationException.class)
    public void testShouldThrowMalformedExceptionOnBadXml() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("<baaad".getBytes());
        ExecutorsDocument.from(in);
    }

    @Test
    public void testShouldCreateDefaultConfiguration() throws Exception {
        HashSet<String> expected = new HashSet<>(Arrays.asList(ExecutorsDocument.MVN_EXECUTOR_CLASS));
        ExecutorsDocument defaultOne = ExecutorsDocument.defaultOne();
        assertThat(defaultOne.executors(), is(expected));
    }

    @Test
    public void testShouldSaveDefaultConfiguration() throws Exception {
        ExecutorsDocument defaultOne = ExecutorsDocument.defaultOne();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        defaultOne.writeTo(out);
        String expectedOut = "<executor>" + ExecutorsDocument.MVN_EXECUTOR_CLASS + "</executor>";
        assertThat(new String(out.toByteArray()).contains(expectedOut));
    }
}
