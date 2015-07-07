package com.github.nyrkovalex.get.me.env;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.github.nyrkovalex.seed.Sys;

public class EnvsTest {

	@Mock
	Sys.Env seedEnv;

	@InjectMocks
	private EnvsEnv env;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testShouldGetCwd() throws Exception {
		when(seedEnv.cwd()).thenReturn("foo");
		assertThat(env.cwd(), is("foo"));
	}

	@Test
	public void testShouldReturnPluginsHome() throws Exception {
		when(seedEnv.userHome()).thenReturn("user");
		assertThat(env.pluginsHome(), is("user" + EnvsEnv.GETME_HOME + EnvsEnv.PLUGINS_HOME));
	}

	@Test
	public void testShouldReturnGetMeHome() throws Exception {
		when(seedEnv.userHome()).thenReturn("user");
		assertThat(env.getMeHome(), is("user" + EnvsEnv.GETME_HOME));
	}

	@Test
	public void testShouldReturnDescriptorFileName() throws Exception {
		assertThat(env.descriptorFileName(), is(EnvsEnv.DESCRIPTOR_FILENAME));
	}

	@Test
	public void testShouldReturnJarPathVariable() throws Exception {
		when(seedEnv.read("JARPATH")).thenReturn("/my/jarpath");
		assertThat(env.jarPath(), is("/my/jarpath"));
	}
}
