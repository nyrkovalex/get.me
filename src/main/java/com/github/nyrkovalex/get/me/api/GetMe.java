package com.github.nyrkovalex.get.me.api;

import java.util.Optional;

public final class GetMe {

	private GetMe() {
		// Module
	}

	public interface Plugin<P> {
		void exec(String workingDir, Optional<P> params) throws Err;
		Optional<Class<P>> paramsClass();
	}

	public static class Err extends Exception {

		public Err() {
		}

		public Err(String message) {
			super(message);
		}

		public Err(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
