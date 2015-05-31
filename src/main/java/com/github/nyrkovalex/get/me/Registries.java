
package com.github.nyrkovalex.get.me;

import com.github.nyrkovalex.seed.Plugins;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class Registries {
  private Registries() {
    // Module
  }

  static class Err extends Exception {

    public Err() {
    }

    Err(String message, Throwable cause) {
      super(message, cause);
    }
  }

  public static <T> Registry<T> registry(Plugins.Repo repo, Class<T> clazz, T... defaults) {
    return new RegistriesRegistry<>(repo, clazz, Arrays.asList(defaults));
  }

  public interface Registry<T> {

    T forName(String className) throws Err;
  }
}

class RegistriesRegistry<T> implements Registries.Registry<T> {

  private static final Logger LOG = Logger.getLogger(RegistriesRegistry.class.getName());
  private final Map<String, T> defaultExecutors;
  private final Plugins.Repo repo;
  private final Class<T> clazz;

  RegistriesRegistry(Plugins.Repo repo, Class<T> clazz, Iterable<T> defaults) {
    defaultExecutors = new HashMap<>();
    defaults.forEach(this::register);
    this.repo = repo;
    this.clazz = clazz;
  }

  private void register(T builder) {
    LOG.fine(() -> "Registering " + builder);
    defaultExecutors.put(builder.getClass().getCanonicalName(), builder);
  }

  @Override
  public T forName(String className) throws Registries.Err {
    return defaultExecutors.containsKey(className)
        ? defaultExecutors.get(className)
        : loadExecutor(className);
  }

  private T loadExecutor(String className) throws Registries.Err {
    try {
      return repo.instanceOf(className, clazz);
    } catch (Plugins.Err ex) {
      throw new Registries.Err("Failed to load class " + className, ex);
    }
  }
}
