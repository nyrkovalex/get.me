package com.github.nyrkovalex.get.me.json;

import com.github.nyrkovalex.seed.Io;
import com.google.gson.Gson;

import java.util.List;
import java.util.Optional;

public final class Jsons {

	private Jsons() {
		// Module
	}

	public static Parser parser() {
		return new com.github.nyrkovalex.get.me.json.Parser(new Gson());
	}

	public interface Description {
		String className();
		<T> Optional<T> params(Optional<Class<T>> clazz);
	}

	public interface Parser {
		List<Description> parse(Io.File file) throws Err;
	}

	public static class Err extends Exception {
		Err(String message, Throwable cause) {
			super(message, cause);
		}
	}
}

