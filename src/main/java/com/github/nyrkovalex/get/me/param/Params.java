package com.github.nyrkovalex.get.me.param;

import java.util.Set;

public final class Params {

	private Params() {
		// Module
	}

	public interface Parsed {

		Set<String> urls();
		boolean debug();
	}

	public static Parsed parse(String[] args) throws Err {
		return ParamsParsed.from(args);
	}

	public static class Err extends Exception {

		private static final String USAGE = ""
				+ "Usage:\n"
				+ "gitdep <url> [, url, ...] [ -debug ]\n"
				+ "\n"
				+ "Parameters:\n"
				+ "url - Url of a dependency repository. Should be in a format understandable by git\n"
				+ "\n"
				+ "Flags:\n"
				+ "-debug - Enable debug output\n";

		@Override
		public String getMessage() {
			return USAGE;
		}
	}
}

