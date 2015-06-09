package com.github.nyrkovalex.get.me.env;

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

