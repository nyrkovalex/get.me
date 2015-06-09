package com.github.nyrkovalex.get.me.mvn;

import com.github.nyrkovalex.get.me.api.GetMe;
import com.github.nyrkovalex.seed.Tests;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.anyObject;

@RunWith(Enclosed.class)
public class MvnBuilderTest extends Tests.Expect {

	public static class BuilderTest extends Tests.Expect {

		@Mock Mvn mvn;
		@Mock Mvn.Runner runner;
		@InjectMocks MvnBuilder builder;

		@Before
		public void setUp() throws Exception {
			given(mvn.run(anyObject())).returns(runner);
		}

		@Test
		public void testShouldUseDefaultTargets() throws Exception {
			builder.exec("foo", Optional.of(new MvnParams()));
			expect(mvn).toHaveCall().run(MvnBuilder.DEFAULT_GOALS);
		}

		@Test
		public void testShouldUseProvidedTargets() throws Exception {
			List<String> targets = Arrays.asList("bar", "baz");
			builder.exec("foo", Optional.of(new MvnParams(targets)));
			expect(mvn).toHaveCall().run(targets);
		}

		@Test
		public void testShouldUseDefaultTargetsOnNullParams() throws Exception {
			Optional<MvnParams> params = Optional.empty();
			builder.exec("foo", params);
			expect(mvn).toHaveCall().run(MvnBuilder.DEFAULT_GOALS);
		}

		@Test
		public void testShouldUseDefaultTargetsOnNullGoals() throws Exception {
			builder.exec("foo", Optional.of(new MvnParams()));
			expect(mvn).toHaveCall().run(MvnBuilder.DEFAULT_GOALS);
		}
	}

	public static class RunnerTest extends Tests.Expect {

		@Mock MvnApi api;
		@Mock Invoker invoker;
		@Mock InvocationRequest req;
		@Mock InvocationResult res;

		List<String> goals = Arrays.asList("foo", "bar");

		@Before
		public void setUp() throws Exception {
			given(api.invocationRequest()).returns(req);
			given(api.invoker()).returns(invoker);
			given(invoker.execute(anyObject())).returns(res);
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
			expect(req).toHaveCall().setPomFile(Paths.get("/tmp", Mvn.POM_XML_NAME).toFile());
		}

		@Test
		public void testShouldRunMaven() throws Exception {
			expect(invoker).toHaveCall().execute(req);
		}

		@Test(expected = GetMe.Err.class)
		public void testShouldThrowOnMavenError() throws Exception {
			given(invoker.execute(req)).failsWith(MavenInvocationException.class);
			new Mvn.Runner(api, goals).in("/tmp");
		}
	}

}
