package com.github.nyrkovalex.get.me;

import com.github.nyrkovalex.seed.Io;
import com.github.nyrkovalex.seed.Plugins;
import java.util.Objects;

public final class Installers {

  private static final Envs.Env ENV = Envs.env();
  private static final Plugins.Loader LOADER = Plugins.loader();

  public static Registries.Registry<Installers.Installer> registry() {
    Plugins.Repo repo = LOADER.repo(ENV.installersHome());
    return Registries.registry(repo, Installers.Installer.class, new ExecJarInstaller());
  }

  private Installers() {
    // Module
  }

  public interface Installer<P> {

    void install(String workingDir, P params) throws Err;
    Class<P> paramsClass();
  }

  public static class Err extends Exception {

    Err() {
    }

    Err(String message) {
      super(message);
    }

    Err(String message, Throwable cause) {
      super(message, cause);
    }
  }
}

class ExecJarParams {
  final String jar;

  ExecJarParams() {
    this.jar = null;
  }

  ExecJarParams(String jar) {
    this.jar = jar;
  }
}

class ExecJarInstaller implements Installers.Installer<ExecJarParams> {

  private final Io.Fs fs;
  private final Envs.Env env;

  public ExecJarInstaller() {
    this.fs = Io.fs();
    this.env = Envs.env();
  }

  ExecJarInstaller(Io.Fs fs, Envs.Env env) {
    this.fs = fs;
    this.env = env;
  }

  @Override
  public void install(String workingDir, ExecJarParams params) throws Installers.Err {
    String targetPath = params.jar;
    try {
      Io.File sourceFile = sourceJar(workingDir, targetPath);
      Io.File targetFile = targetFile(sourceFile.name());
      sourceFile.copyTo(targetFile);
    } catch (Io.Err err) {
      throw new Installers.Err(
          String.format("Failed to copy %s to %s", targetPath, env.jarPath()),
          err
      );
    }
  }

  private Io.File targetFile(String sourceFile) throws Io.Err, Installers.Err {
    String jarPath = env.jarPath();
    if (Objects.isNull(jarPath) || jarPath.isEmpty()) {
      throw new Installers.Err("JARPATH environment variable is not set");
    }
    Io.File targetFile = fs.file(jarPath, sourceFile);
    return targetFile;
  }

  private Io.File sourceJar(String workingDir, String targetPath) throws Installers.Err, Io.Err {
    if (Objects.isNull(targetPath) || targetPath.isEmpty()) {
      throw new Installers.Err("No \"jar\" param provided");
    }
    Io.File sourceFile = fs.file(workingDir, targetPath);
    if (!sourceFile.exists()) {
      throw new Installers.Err(targetPath + " does not exist");
    }
    return sourceFile;
  }

  @Override
  public Class<ExecJarParams> paramsClass() {
    return ExecJarParams.class;
  }

}
