package com.github.nyrkovalex.get.me;

import com.github.nyrkovalex.get.me.api.Builders;
import com.github.nyrkovalex.get.me.api.Installers;
import com.github.nyrkovalex.seed.Io;
import com.github.nyrkovalex.seed.Seed;
import java.util.logging.Logger;

final class App {

  private static final Logger LOG = Seed.logger(App.class);

  public static void main(String... args) throws Exception {
    Params.Parsed params = parseArguments(args);
    Seed.Logging.init(params.debug(), App.class);
    App gitdep = new App(params);
    gitdep.run();
  }

  private static Params.Parsed parseArguments(String[] args) {
    try {
      return Params.parse(args);
    } catch (Params.Err ex) {
      System.out.println(ex.getMessage());
      System.exit(1);
    }
    throw new AssertionError("Will never reach this");
  }

  private final Envs.Env env = Envs.env();
  private final Params.Parsed params;
  private final Registries.Registry<Builders.Builder> buildersRegistry = Registries.builderRegistry();
  private final Registries.Registry<Installers.Installer> installerRegistry = Registries.installerRegistry();
  private final Git.Cloner cloner = Git.cloner();
  private final Jsons.Parser parser = Jsons.parser();
  private final Io.Fs fs = Io.fs();

  private App(Params.Parsed params) {
    this.params = params;
  }

  private void run() throws Exception {
    for (String url : params.urls()) {
      buildTarget(url);
    }
  }

  private void buildTarget(String url) throws Exception {
    Io.Dir tempDir = fs.tempDir();
    try {
      cloner.clone(url).to(tempDir.path());
      Io.File descriptorFile = fs.file(tempDir.path(), env.descriptorFileName());
      Jsons.Descriptor parsed = parser.parse(descriptorFile);
      build(parsed, tempDir.path());
      install(parsed, tempDir.path());
    } finally {
      tempDir.delete();
    }
  }

  private void install(Jsons.Descriptor parsed, String workingDir) throws Exception {
    LOG.info("Installing...");
    Jsons.Description installerDescription = parsed.installer();
    Installers.Installer installer = installerRegistry.forName(installerDescription.className());
    Object installerParams = installerDescription.params(installer.paramsClass());
    installer.install(workingDir, installerParams);
  }

  private void build(Jsons.Descriptor parsed, String workingDir) throws Builders.Err, Registries.Err {
    LOG.info("Building...");
    Jsons.Description builderDescription = parsed.builder();
    Builders.Builder builder = buildersRegistry.forName(builderDescription.className());
    Object builderParams = builderDescription.params(builder.paramsClass());
    builder.build(workingDir, builderParams);
  }

}
