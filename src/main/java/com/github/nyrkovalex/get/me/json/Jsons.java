package com.github.nyrkovalex.get.me.json;

import com.google.gson.Gson;
import com.gtihub.nyrkovalex.seed.nio.Fs;

public final class Jsons {

	private Jsons() {
		// Module
	}

	public static Parser parser() {
		return new Parser(new Gson(), Fs.instance());
	}

	public static class Err extends Exception {
		Err(String message, Throwable cause) {
			super(message, cause);
		}
	}
}

