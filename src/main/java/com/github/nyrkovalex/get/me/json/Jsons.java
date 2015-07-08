package com.github.nyrkovalex.get.me.json;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.gtihub.nyrkovalex.seed.nio.Fs;

public final class Jsons {

	private Jsons() {
		// Module
	}

	public static Parser parser() {
		return new com.github.nyrkovalex.get.me.json.Parser(new Gson(), Fs.instance());
	}

	public interface Description {
		String className();
		<T> Optional<T> params(Optional<Class<T>> clazz);
	}

	public interface Parser {
		List<Description> parse(Path path) throws Err;
	}

	public static class Err extends Exception {
		Err(String message, Throwable cause) {
			super(message, cause);
		}
	}
}

