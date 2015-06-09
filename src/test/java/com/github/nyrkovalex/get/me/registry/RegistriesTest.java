package com.github.nyrkovalex.get.me.registry;

import com.github.nyrkovalex.get.me.api.GetMe;
import com.github.nyrkovalex.get.me.mvn.MvnBuilder;
import com.github.nyrkovalex.seed.Plugins;
import com.github.nyrkovalex.seed.Tests;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;

public class RegistriesTest extends Tests.Expect {

	@Mock Plugins.Repo repo;
	@Mock GetMe.Plugin externalPlugin;
	Registries.Registry<GetMe.Plugin> registry;

	@Before
	public void setUp() throws Exception {
		registry = new RegistriesRegistry<>(repo, GetMe.Plugin.class, Collections.singletonList(new MvnBuilder()));
	}

	@Test
	public void testShouldReturnDefaultPlugin() throws Exception {
		GetMe.Plugin found = registry.forName(MvnBuilder.class.getCanonicalName());
		expect(found.getClass().equals(MvnBuilder.class)).toBe(true);
	}

	@Test
	public void testShouldLoadPluginFromRepo() throws Exception {
		given(repo.instanceOf("ExternalPlugin", GetMe.Plugin.class)).returns(externalPlugin);
		expect(registry.forName("ExternalPlugin")).toBe(externalPlugin);
	}

	@Test(expected = Registries.Err.class)
	public void testShouldThrowWhenNoExecutorCanBeLoaded() throws Exception {
		given(repo.instanceOf("ExternalPlugin", GetMe.Plugin.class)).failsWith(Plugins.Err.class);
		registry.forName("ExternalPlugin");
	}
}
