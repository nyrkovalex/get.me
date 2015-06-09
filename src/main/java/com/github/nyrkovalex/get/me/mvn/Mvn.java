package com.github.nyrkovalex.get.me.mvn;

import com.github.nyrkovalex.get.me.api.GetMe;
import com.github.nyrkovalex.seed.Seed;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.util.cli.CommandLineException;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

class Mvn {

	static final String POM_XML_NAME = "pom.xml";
	private static final Mvn INSTANCE = new Mvn();
	private static final MvnApi API = new MvnApi();

	public static Mvn instance() {
		return INSTANCE;
	}

	Runner run(List<String> targets) {
		return new Runner(API, targets);
	}

	static class Runner {

		private static final Logger LOG = Seed.logger(Runner.class);
		private final MvnApi api;
		private final List<String> goals;
		private boolean enableOutput = false;

		Runner(MvnApi api, List<String> goals) {
			this.api = api;
			this.goals = Collections.unmodifiableList(new ArrayList<>(goals));
		}

		public Runner enableOutput(boolean enable) {
			this.enableOutput = enable;
			return this;
		}

		public void in(String path) throws GetMe.Err {
			LOG.fine(() -> String.format("running `mvn %s` in %s", Seed.Strings.join(" ", goals), path));
			InvocationRequest request = createInvocationRequest(path);
			Invoker invoker = createMvnInvoker(path);
			run(request, invoker);
			LOG.fine(() -> String.format("completed `mvn %s` in %s", Seed.Strings.join(" ", goals), path));
		}

		private void run(InvocationRequest request, Invoker invoker) throws GetMe.Err {
			try {
				InvocationResult result = invoker.execute(request);
				CommandLineException executionException = result.getExecutionException();
				if (!Objects.isNull(executionException)) {
					throw executionException;
				}
				if (result.getExitCode() != 0) {
					LOG.severe(() -> "Failed to build project :(");
					throw new GetMe.Err("Maven execution failed");
				}
			} catch (MavenInvocationException | CommandLineException e) {
				throw new GetMe.Err("Maven execution failed", e);
			}
		}

		private Invoker createMvnInvoker(String path) {
			Invoker invoker = api.invoker();
			invoker.setWorkingDirectory(Paths.get(path).toFile());
			if (!enableOutput) {
				invoker.setOutputHandler(null);
			}
			return invoker;
		}

		private InvocationRequest createInvocationRequest(String path) {
			InvocationRequest request = api.invocationRequest();
			request.setPomFile(Paths.get(path, POM_XML_NAME).toFile());
			request.setGoals(goals);
			return request;
		}
	}

}
