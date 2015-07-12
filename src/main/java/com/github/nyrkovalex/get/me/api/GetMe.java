package com.github.nyrkovalex.get.me.api;

import java.nio.file.Path;
import java.util.Optional;

/**
 * GetMe API entry point. Contains everything needed for plugin implementation.
 */
public final class GetMe {

	private GetMe() {
		// Module
	}

	public interface Plugin<P> {
		void exec(ExecutionContext context, Optional<P> params) throws PluginException;
		Optional<Class<P>> paramsClass();
	}

	public interface ExecutionContext {
		boolean isDebug();
		Path getCwd();
	}

	public static class PluginException extends Exception {

		public PluginException(String message) {
			super(message);
		}

		public PluginException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	public static class Environment {

		private final static Environment INSTANCE = new Environment();
		final static String PLUGINS_HOME = "/plugins";
		final static String GETME_HOME = "/.get.me";
		private final String userHome;

		private Environment() {
			userHome = System.getenv("user.home");
		}

		public String pluginsHome() {
			return getMeHome() + PLUGINS_HOME;
		}

		public String getMeHome() {
			return userHome + GETME_HOME;
		}

		private static Environment instance() {
			return INSTANCE;
		}
	}

	public static Environment environment() {
		return Environment.instance();
	}
}
