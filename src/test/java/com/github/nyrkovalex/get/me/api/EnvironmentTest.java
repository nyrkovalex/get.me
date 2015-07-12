package com.github.nyrkovalex.get.me.api;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EnvironmentTest {

	private final static String USER_HOME = System.getenv("user.home");
	private GetMe.Environment environment;

	@Before
	public void setUp() throws Exception {
		environment = GetMe.environment();
	}

	@Test
	public void testShouldReturnPluginsHome() throws Exception {
		assertThat(environment.pluginsHome(), is(USER_HOME + GetMe.Environment.GETME_HOME + GetMe.Environment.PLUGINS_HOME));
	}

	@Test
	public void testShouldReturnGetMeHome() throws Exception {
		assertThat(environment.getMeHome(), is(USER_HOME + GetMe.Environment.GETME_HOME));
	}
}
