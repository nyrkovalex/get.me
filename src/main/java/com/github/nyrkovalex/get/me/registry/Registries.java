package com.github.nyrkovalex.get.me.registry;

import com.github.nyrkovalex.get.me.api.GetMe;
import com.github.nyrkovalex.get.me.env.Envs;
import com.github.nyrkovalex.seed.Plugins;

public final class Registries {

	private Registries() {
		// Module
	}

	private static final Envs.Env ENV = Envs.env();
	private static final Plugins.Loader LOADER = Plugins.loader();

	public static Registries.Registry<GetMe.Plugin> pluginRegistry() {
		Plugins.Repo repo = LOADER.repo(ENV.pluginsHome());
		return new RegistriesRegistry<>(repo, GetMe.Plugin.class);
	}

	public static class Err extends Exception {

		public Err() {
		}

		Err(String message, Throwable cause) {
			super(message, cause);
		}
	}

	public interface Registry<T> {
		T forName(String className) throws Err;
	}
}
