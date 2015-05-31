package com.github.nyrkovalex.get.me;

import com.github.nyrkovalex.get.me.install.ExecJarParams;
import com.github.nyrkovalex.get.me.mvn.MvnParams;
import com.github.nyrkovalex.seed.Io;
import com.github.nyrkovalex.seed.Tests;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.StringReader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class JsonsTest extends Tests.Expect {

  private static final String JSON = ""
      + "{\n"
      + "  \"builder\": {\n"
      + "    \"class\": \"com.github.nyrkovalex.get.me.mvn.MvnBuilder\",\n"
      + "    \"params\": {\n"
      + "      \"goals\": [ \"clean\", \"package\" ]\n"
      + "    }\n"
      + "  },\n"
      + "  \"installer\": {\n"
      + "    \"class\": \"com.github.nyrkovalex.get.me.install.ExecJarInstaller\",\n"
      + "    \"params\": {\n"
      + "      \"jar\": \"target/get.me.jar\"\n"
      + "    }\n"
      + "  }\n"
      + "}\n"
      + "";

  @Mock Io.File file;
  Jsons.Descriptor parsed;

  @Before
  public void setUp() throws Exception {
    given(file.reader()).returns(new BufferedReader(new StringReader(JSON)));
    DescriptorParser parser = new DescriptorParser(new Gson());
    parsed = parser.parse(file);
  }

  @Test
  public void testShouldParseBuilder() throws Exception {
    expect(parsed.builder().className()).toBe("com.github.nyrkovalex.get.me.mvn.MvnBuilder");
  }

  @Test
  public void testShouldParseInstaller() throws Exception {
    expect(parsed.installer().className()).toBe("com.github.nyrkovalex.get.me.install.ExecJarInstaller");
  }

  @Test
  public void testShouldParseBulderParams() throws Exception {
    MvnParams params = parsed.builder().params(MvnParams.class);
    expect(params.goals.get(0)).toBe("clean");
    expect(params.goals.get(1)).toBe("package");
  }

  @Test
  public void testShouldParseInstallerParams() throws Exception {
    ExecJarParams params = parsed.installer().params(ExecJarParams.class);
    expect(params.jar).toBe("target/get.me.jar");
  }
}
