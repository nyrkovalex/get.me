package com.github.nyrkovalex.get.me;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.github.nyrkovalex.get.me.api.GetMe;
import com.github.nyrkovalex.get.me.env.Envs;
import com.github.nyrkovalex.get.me.git.Git;
import com.github.nyrkovalex.get.me.json.Jsons;
import com.github.nyrkovalex.get.me.param.Params;
import com.github.nyrkovalex.get.me.param.RepoUrl;
import com.github.nyrkovalex.get.me.registry.Registries;
import com.github.nyrkovalex.seed.logging.Logging;
import com.gtihub.nyrkovalex.seed.nio.Fs;

final class App {

	private static final Logger LOG = Logging.logger(App.class);

	public static void main(String... args) throws Exception {
		Params.Parsed params = parseArguments(args);
		Logging.init(params.isDebug(), App.class);
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
	private final Fs fileSys = Fs.instance();

	private App(Params.Parsed params) {
		this.params = params;
	}

	private void run() throws Exception {
		for (RepoUrl url : params.getUrls()) {
			buildTarget(url);
		}
	}

	private void buildTarget(RepoUrl url) throws Exception {
		Path tempDir = fileSys.tempDir("get.me-");
		try {
			cloner.clone(url.getUrl())
					.branch(url.getBranch())
					.enableOutput(params.isDebug())
					.to(tempDir);
			Path descriptorFile = tempDir.resolve(env.descriptorFileName());
			List<Jsons.Description> parsed = parser.parse(descriptorFile);
			for (Jsons.Description p : parsed) {
				runPlugin(tempDir, p);
			}
		} finally {
			fileSys.deleteWithContents(tempDir);
		}
	}

  // We don't know the generic argument type here,
	// it's client's job to provide correct class for JSON deserialization
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void runPlugin(Path path, Jsons.Description builderDescription) throws Registries.Err, GetMe.Err {
		LOG.info(() -> "Running " + builderDescription.className());
		GetMe.Plugin builder = pluginsRegistry.forName(builderDescription.className());
		Optional builderParams = builderDescription.params(builder.paramsClass());
		builder.exec(path, builderParams);
	}

}
