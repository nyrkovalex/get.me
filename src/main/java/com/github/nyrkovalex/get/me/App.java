package com.github.nyrkovalex.get.me;

import com.github.nyrkovalex.get.me.api.GetMe;
import com.github.nyrkovalex.get.me.git.Git;
import com.github.nyrkovalex.get.me.json.Description;
import com.github.nyrkovalex.get.me.json.Jsons;
import com.github.nyrkovalex.get.me.json.Parser;
import com.github.nyrkovalex.get.me.param.Params;
import com.github.nyrkovalex.get.me.param.RepoUrl;
import com.github.nyrkovalex.get.me.param.WrongUsageException;
import com.github.nyrkovalex.seed.logging.Logging;
import com.github.nyrkovalex.seed.plugins.Plugins;
import com.gtihub.nyrkovalex.seed.nio.Fs;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

final class App {

	private static final GetMe.Environment GETME_ENVIRONMENT = GetMe.environment();
	private static final Plugins.Loader LOADER = Plugins.loader();
	private static final Logger LOG = Logging.logger(App.class);
	private static final String DESCRIPTOR_FILENAME = "get.me.json";


	public static void main(String... args) throws Exception {
		Params params;
		try {
			params = Params.from(args);
		} catch (WrongUsageException e) {
			System.out.println(e.getMessage());
			System.exit(1);
			return;
		}
		Logging.init(params.isDebug(), App.class);
		App getMe = new App(params);
		getMe.run();
	}

	private final Params params;
	private final Git.Cloner cloner = Git.cloner();
	private final Parser parser = Jsons.parser();
	private final Fs fileSys = Fs.instance();

	private App(Params params) {
		this.params = params;
	}

	private void run() throws Exception {
		for (RepoUrl url : params.getUrls()) {
			buildTarget(url, params.isDebug());
		}
	}

	private void buildTarget(RepoUrl url, boolean debug) throws Exception {
		Path tempDir = fileSys.tempDir("get.me-");
		try {
			cloner.clone(url.getUrl())
					.branch(url.getBranch())
					.enableOutput(params.isDebug())
					.to(tempDir);
			Path descriptorFile = tempDir.resolve(DESCRIPTOR_FILENAME);
			List<Description> parsed = parser.parseDescription(descriptorFile);
			for (Description p : parsed) {
				runPlugin(new PluginExecutionContext(tempDir, debug), p);
			}
		} finally {
			fileSys.deleteWithContents(tempDir);
		}
	}

  // We don't know the generic argument type here,
	// it's client's job to provide correct class for JSON deserialization
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void runPlugin(GetMe.ExecutionContext context, Description builderDescription)
			throws ReflectiveOperationException, GetMe.PluginException {
		LOG.info(() -> "Running " + builderDescription.className());
		Plugins.Repo repo = LOADER.repo(GETME_ENVIRONMENT.pluginsHome());
		GetMe.Plugin builder = repo.instanceOf(builderDescription.className(), GetMe.Plugin.class);
		Optional builderParams = builderDescription.params(builder.paramsClass());
		builder.exec(context, builderParams);
	}

	private static class PluginExecutionContext implements GetMe.ExecutionContext {

		private final Path cwd;
		private final boolean debug;

		private PluginExecutionContext(Path cwd, boolean debug) {
			this.cwd = cwd;
			this.debug = debug;
		}

		@Override
		public boolean isDebug() {
			return debug;
		}

		@Override
		public Path getCwd() {
			return cwd;
		}
	}
}
