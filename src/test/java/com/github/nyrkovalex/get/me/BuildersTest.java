package com.github.nyrkovalex.get.me;

import com.github.nyrkovalex.seed.Tests;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;

@RunWith(Enclosed.class)
public class BuildersTest extends Tests.Expect {

  public static class MvnBuilderTest extends Tests.Expect {

    @Mock Mvn mvn;
    @Mock Mvn.Runner runner;
    @InjectMocks MvnBuilder builder;

    @Before
    public void setUp() throws Exception {
      given(mvn.run(any(List.class))).returns(runner);
    }

    @Test
    public void testShouldUseDefaultTargets() throws Exception {
      builder.build("foo", new MvnBuilderParams());
      expect(mvn).toHaveCall().run(MvnBuilder.DEFAULT_GOALS);
    }

    @Test
    public void testShouldUseProvidedTargets() throws Exception {
      List<String> targets = Arrays.asList("bar", "baz");
      builder.build("foo", new MvnBuilderParams(targets));
      expect(mvn).toHaveCall().run(targets);
    }
  }

  public static class MvnRunnerTest extends Tests.Expect {

    @Mock MvnApi api;
    @Mock Invoker invoker;
    @Mock InvocationRequest req;

    List<String> goals = Arrays.asList("foo", "bar");

    @Before
    public void setUp() throws Exception {
      given(api.invocationRequest()).returns(req);
      given(api.invoker()).returns(invoker);
      new Mvn.Runner(api, goals).in("/tmp");
    }

    @Test
    public void testShouldSetWokingDir() throws Exception {
      expect(invoker).toHaveCall().setWorkingDirectory(Paths.get("/tmp").toFile());
    }

    @Test
    public void testShouldSetTargets() throws Exception {
      expect(req).toHaveCall().setGoals(goals);
    }

    @Test
    public void testShouldSetPomFile() throws Exception {
      expect(req).toHaveCall().setPomFile(Paths.get("/tmp", MvnBuilder.POM_XML_NAME).toFile());
    }

    @Test
    public void testShouldRunMaven() throws Exception {
      expect(invoker).toHaveCall().execute(req);
    }

    @Test(expected = Builders.Err.class)
    public void testShouldThrowOnMavenError() throws Exception {
      given(invoker.execute(req)).failsWith(MavenInvocationException.class);
      new Mvn.Runner(api, goals).in("/tmp");
    }
  }
}
