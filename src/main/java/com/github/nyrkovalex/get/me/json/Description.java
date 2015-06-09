package com.github.nyrkovalex.get.me.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Optional;

class Description implements Jsons.Description {

	private final String className;
	private final JsonObject params;
	private final static Gson GSON = new Gson();

	public Description(String className, JsonObject params) {
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
