package com.github.nyrkovalex.get.me.registry;

import java.util.logging.Logger;

import com.github.nyrkovalex.seed.Plugins;

class RegistriesRegistry<T> implements Registries.Registry<T> {

	private static final Logger LOG = Logger.getLogger(RegistriesRegistry.class.getName());
	private final Plugins.Repo repo;
	private final Class<T> clazz;

	RegistriesRegistry(Plugins.Repo repo, Class<T> clazz) {
		this.repo = repo;
		this.clazz = clazz;
	}

	@Override
	public T forName(String className) throws Registries.Err {
		try {
			return repo.instanceOf(className, clazz);
		} catch (Plugins.Err ex) {
			throw new Registries.Err("Failed to load class " + className, ex);
		}
	}
}
