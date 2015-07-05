package com.github.nyrkovalex.get.me;

import com.github.nyrkovalex.get.me.api.GetMe;
import com.github.nyrkovalex.get.me.env.Envs;
import com.github.nyrkovalex.get.me.git.Git;
import com.github.nyrkovalex.get.me.json.Jsons;
import com.github.nyrkovalex.get.me.param.Params;
import com.github.nyrkovalex.get.me.param.RepoUrl;
import com.github.nyrkovalex.get.me.registry.Registries;
import com.github.nyrkovalex.seed.Io;
import com.github.nyrkovalex.seed.Seed;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

final class App {

	private static final Logger LOG = Seed.logger(App.class);

	public static void main(String... args) throws Exception {
		Params.Parsed params = parseArguments(args);
		Seed.Logging.init(params.isDebug(), App.class);
		App getMe = new App(params);
		getMe.run();
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
	private final Registries.Registry<GetMe.Plugin> pluginsRegistry = Registries.pluginRegistry();
	private final Git.Cloner cloner = Git.cloner();
	private final Jsons.Parser parser = Jsons.parser();
	private final Io.Fs fs = Io.fs();

	private App(Params.Parsed params) {
		this.params = params;
	}

	private void run() throws Exception {
		for (RepoUrl url : params.getUrls()) {
			buildTarget(url);
		}
	}

	private void buildTarget(RepoUrl url) throws Exception {
		Io.Dir tempDir = fs.tempDir();
		try {
			cloner.clone(url.getUrl())
					.branch(url.getBranch())
					.enableOutput(params.isDebug())
					.to(tempDir.path());
			Io.File descriptorFile = fs.file(tempDir.path(), env.descriptorFileName());
			List<Jsons.Description> parsed = parser.parse(descriptorFile);
			for (Jsons.Description p : parsed) {
				runPlugin(tempDir.path(), p);
			}
		} finally {
			tempDir.delete();
		}
	}

  // We don't know the generic argument type here,
	// it's client's job to provide correct class for JSON deserialization
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void runPlugin(String workingDir, Jsons.Description builderDescription) throws Registries.Err, GetMe.Err {
		LOG.info(() -> "Running " + builderDescription.className());
		GetMe.Plugin builder = pluginsRegistry.forName(builderDescription.className());
		Optional builderParams = builderDescription.params(builder.paramsClass());
		builder.exec(workingDir, builderParams);
	}

}
