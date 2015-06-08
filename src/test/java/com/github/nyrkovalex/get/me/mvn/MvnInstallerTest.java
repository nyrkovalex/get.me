package com.github.nyrkovalex.get.me.mvn;

import com.github.nyrkovalex.seed.Tests;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.anyObject;

public class MvnInstallerTest extends Tests.Expect {

	@Mock Mvn mvn;
	@Mock Mvn.Runner runner;
	@InjectMocks MvnInstaller installer;

	@Before
	public void setUp() throws Exception {
		given(mvn.run(anyObject())).returns(runner);
	}

	@Test
	public void testShouldRunDefaultGoals() throws Exception {
		installer.exec("/tmp", Optional.of(new MvnParams(null)));
		expect(mvn).toHaveCall().run(MvnInstaller.DEFAULT_GOALS);
	}

	@Test
	public void testShouldUseProvidedGoals() throws Exception {
		List<String> goals = Arrays.asList("foo", "bar");
		MvnParams params = new MvnParams(goals);
		installer.exec("/tmp", Optional.of(params));
		expect(mvn).toHaveCall().run(goals);
	}

	@Test
	public void testShouldRunDefaultGoalsWithNullParams() throws Exception {
		installer.exec("/tmp", Optional.<MvnParams>empty());
		expect(mvn).toHaveCall().run(MvnInstaller.DEFAULT_GOALS);
	}

	@Test
	public void testShouldRunDefaultGoalsWithEmptyParams() throws Exception {
		installer.exec("/tmp", Optional.of(new MvnParams()));
		expect(mvn).toHaveCall().run(MvnInstaller.DEFAULT_GOALS);
	}

}
