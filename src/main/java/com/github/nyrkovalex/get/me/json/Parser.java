package com.github.nyrkovalex.get.me.json;

import com.github.nyrkovalex.seed.Io;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Parser implements Jsons.Parser {

	private final Gson gson;

	public Parser(Gson gson) {
		this.gson = gson;
	}

	@Override
	public List<Jsons.Description> parse(Io.File file) throws Jsons.Err {
		try (BufferedReader reader = file.reader()) {
			JsonArray rootArray = gson.fromJson(reader, JsonArray.class);
			List<Jsons.Description> plugins = new ArrayList<>(rootArray.size());
			rootArray.forEach(item -> plugins.add(readDescription(item.getAsJsonObject())));
			return plugins;
		} catch (IOException ex) {
			throw new Jsons.Err("Could not parse " + file.path(), ex);
		}
	}

	private Jsons.Description readDescription(JsonObject rootJsonObject) {
		String className = rootJsonObject.getAsJsonPrimitive("class").getAsString();
		JsonObject params = rootJsonObject.getAsJsonObject("params");
		return new Description(className, params);
	}

}
