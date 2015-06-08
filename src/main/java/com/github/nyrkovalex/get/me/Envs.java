package com.github.nyrkovalex.get.me;

import com.github.nyrkovalex.seed.Sys;

public final class Envs {

	private final static Sys.Env SEED_ENV = Sys.env();

	public interface Env {

		String getMeHome();

		String pluginsHome();

		String cwd();

		String descriptorFileName();

		String jarPath();
	}

	public static Env env() {
		return new EnvsEnv(SEED_ENV);
	}
}

class EnvsEnv implements Envs.Env {

	final static String PLUGINS_HOME = "/plugins";
	final static String GETME_HOME = "/.get.me";
	final static String DESCRIPTOR_FILENAME = "get.me.json";
	private final Sys.Env env;

	public EnvsEnv(Sys.Env env) {
		this.env = env;
	}

	@Override
	public String pluginsHome() {
		return getMeHome() + PLUGINS_HOME;
	}

	@Override
	public String cwd() {
		return env.cwd();
	}

	@Override
	public String getMeHome() {
		return env.userHome() + GETME_HOME;
	}

	@Override
	public String descriptorFileName() {
		return DESCRIPTOR_FILENAME;
	}

	@Override
	public String jarPath() {
		return env.read("JARPATH");
	}

}
