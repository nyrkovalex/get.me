
package com.github.nyrkovalex.get.me.install;

import com.github.nyrkovalex.get.me.Envs;
import com.github.nyrkovalex.get.me.api.Installers;
import com.github.nyrkovalex.seed.Io;
import java.util.Objects;

public class ExecJarInstaller implements Installers.Installer<ExecJarParams> {
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
      throw new Installers.Err(String.format("Failed to copy %s to %s", targetPath, env.jarPath()), err);
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
