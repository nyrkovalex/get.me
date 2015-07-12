package com.github.nyrkovalex.get.me.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Optional;

public class Description {

	private final String className;
	private final JsonObject params;
	private final Gson gson;

	Description(String className, JsonObject params, Gson gson) {
		this.className = className;
		this.params = params;
		this.gson = gson;
	}

	public String className() {
		return className;
	}

	public <T> Optional<T> params(Optional<Class<T>> clazz) {
		return clazz.map(c -> gson.fromJson(params, c));
	}
}
