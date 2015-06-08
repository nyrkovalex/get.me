package com.github.nyrkovalex.get.me;

import com.github.nyrkovalex.seed.Io;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

final class Jsons {

	private Jsons() {
		// Module
	}

	public static Parser parser() {
		return new DescriptorParser(new Gson());
	}

	public interface Description {

		String className();

		<T> Optional<T> params(Optional<Class<T>> clazz);
	}

	public interface Descriptor {

		Description builder();

		Description installer();
	}

	public interface Parser {

		Descriptor parse(Io.File file) throws Err;
	}

	public static class Err extends Exception {

		Err(Throwable cause) {
			super(cause);
		}

		Err(String message, Throwable cause) {
			super(message, cause);
		}
	}
}

class JsonsDescription implements Jsons.Description {

	private final String className;
	private final JsonObject params;
	private final static Gson GSON = new Gson();

	public JsonsDescription(String className, JsonObject params) {
		this.className = className;
		this.params = params;
	}

	@Override
	public String className() {
		return className;
	}

	@Override
	public <T> Optional<T> params(Optional<Class<T>> clazz) {
		return clazz.map(c -> GSON.fromJson(params, c));
	}
}

class JsonsDescriptor implements Jsons.Descriptor {

	private final Jsons.Description installer;
	private final Jsons.Description builder;

	public JsonsDescriptor(Jsons.Description installer, Jsons.Description builder) {
		this.installer = installer;
		this.builder = builder;
	}

	@Override
	public Jsons.Description builder() {
		return builder;
	}

	@Override
	public Jsons.Description installer() {
		return installer;
	}

}

class DescriptorParser implements Jsons.Parser {

	private final Gson gson;

	public DescriptorParser(Gson gson) {
		this.gson = gson;
	}

	@Override
	public Jsons.Descriptor parse(Io.File file) throws Jsons.Err {
		try {
			BufferedReader reader = file.reader();
			JsonObject rootJsonObject = gson.fromJson(reader, JsonObject.class);
			Jsons.Description builder = readDescription(rootJsonObject.getAsJsonObject("builder"));
			Jsons.Description installer = readDescription(rootJsonObject.getAsJsonObject("installer"));
			return new JsonsDescriptor(installer, builder);
		} catch (IOException ex) {
			throw new Jsons.Err("Could not parse " + file.path(), ex);
		}
	}

	private Jsons.Description readDescription(JsonObject rootJsonObject) {
		String className = rootJsonObject.getAsJsonPrimitive("class").getAsString();
		JsonObject params = rootJsonObject.getAsJsonObject("params");
		return new JsonsDescription(className, params);
	}

}
