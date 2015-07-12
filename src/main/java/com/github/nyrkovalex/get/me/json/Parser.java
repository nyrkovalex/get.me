package com.github.nyrkovalex.get.me.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gtihub.nyrkovalex.seed.nio.Fs;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Parser {

	private final Gson gson;
	private final Fs fs;

	Parser(Gson gson, Fs fs) {
		this.gson = gson;
		this.fs = fs;
	}

	public List<Description> parseDescription(Path path) throws Jsons.Err {
		try (BufferedReader reader = fs.newBufferedReader(path)) {
			JsonArray rootArray = gson.fromJson(reader, JsonArray.class);
			List<Description> plugins = new ArrayList<>(rootArray.size());
			rootArray.forEach(item -> plugins.add(readDescription(item.getAsJsonObject())));
			return plugins;
		} catch (IOException ex) {
			throw new Jsons.Err("Could not parse " + path, ex);
		}
	}

	private Description readDescription(JsonObject rootJsonObject) {
		String className = rootJsonObject.getAsJsonPrimitive("class").getAsString();
		JsonObject params = rootJsonObject.getAsJsonObject("params");
		return new Description(className, params, gson);
	}

}
