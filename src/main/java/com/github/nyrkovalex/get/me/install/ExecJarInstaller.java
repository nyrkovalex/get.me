package com.github.nyrkovalex.get.me.install;

import com.github.nyrkovalex.get.me.Envs;
import com.github.nyrkovalex.get.me.api.GetMe;
import com.github.nyrkovalex.seed.Io;
import java.util.Objects;

public class ExecJarInstaller implements GetMe.Installer<ExecJarParams> {

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
  public void install(String workingDir, ExecJarParams params) throws GetMe.Err {
    String targetPath = params.jar;
    try {
      Io.File sourceFile = sourceJar(workingDir, targetPath);
      Io.File targetFile = targetFile(sourceFile.name());
      sourceFile.copyTo(targetFile);
    } catch (Io.Err err) {
      throw new GetMe.Err(String.format("Failed to copy %s to %s", targetPath, env.jarPath()), err);
    }
  }

  private Io.File targetFile(String sourceFile) throws Io.Err, GetMe.Err {
    String jarPath = env.jarPath();
    if (Objects.isNull(jarPath) || jarPath.isEmpty()) {
      throw new GetMe.Err("JARPATH environment variable is not set");
    }
    Io.File targetFile = fs.file(jarPath, sourceFile);
    return targetFile;
  }

  private Io.File sourceJar(String workingDir, String targetPath) throws GetMe.Err, Io.Err {
    if (Objects.isNull(targetPath) || targetPath.isEmpty()) {
      throw new GetMe.Err("No \"jar\" param provided");
    }
    Io.File sourceFile = fs.file(workingDir, targetPath);
    if (!sourceFile.exists()) {
      throw new GetMe.Err(sourceFile.path() + " does not exist");
    }
    return sourceFile;
  }

  @Override
  public Class<ExecJarParams> paramsClass() {
    return ExecJarParams.class;
  }

}
