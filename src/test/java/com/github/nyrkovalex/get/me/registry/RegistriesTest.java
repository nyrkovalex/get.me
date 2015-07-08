package com.github.nyrkovalex.get.me.registry;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.github.nyrkovalex.get.me.api.GetMe;
import com.github.nyrkovalex.seed.plugins.Plugins;

public class RegistriesTest {

	@Mock
	Plugins.Repo repo;

	@Mock
	GetMe.Plugin<?> externalPlugin;

	Registries.Registry<GetMe.Plugin> registry;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		registry = new RegistriesRegistry<>(repo, GetMe.Plugin.class);
	}

	@Test
	public void testShouldLoadPluginFromRepo() throws Exception {
		when(repo.instanceOf("ExternalPlugin", GetMe.Plugin.class)).thenReturn(externalPlugin);
		assertThat(registry.forName("ExternalPlugin"), is(externalPlugin));
	}

	@Test(expected = Registries.Err.class)
	public void testShouldThrowWhenNoExecutorCanBeLoaded() throws Exception {
		when(repo.instanceOf("ExternalPlugin", GetMe.Plugin.class)).thenThrow(Plugins.Err.class);
		registry.forName("ExternalPlugin");
	}
}
