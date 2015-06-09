package com.github.nyrkovalex.get.me.install;

import com.github.nyrkovalex.get.me.env.Envs;
import com.github.nyrkovalex.get.me.api.GetMe;
import com.github.nyrkovalex.seed.Io;

import java.util.Optional;

public class PluginInstaller implements GetMe.Plugin<JarParams> {

	private final Envs.Env env;
	private final Io.Fs fs;

	public PluginInstaller() {
		this(Envs.env(), Io.fs());
	}

	PluginInstaller(Envs.Env env, Io.Fs fs) {
		this.env = env;
		this.fs = fs;
	}

	@Override
	public void exec(String workingDir, Optional<JarParams> params) throws GetMe.Err {
		JarParams jarParams = params.orElseThrow(
				() -> new GetMe.Err("`jar` parameter must be provided"));
		try {
			Io.Dir pluginsDir = fs.dir(env.pluginsHome());
			Io.File targetJar = fs.file(workingDir, jarParams.jar);
			targetJar.copyTo(pluginsDir);
		} catch (Io.Err err) {
			throw new GetMe.Err(
					String.format("Failed to copy %s to %s", jarParams.jar, env.pluginsHome())
			);
		}
	}

	@Override
	public Optional<Class<JarParams>> paramsClass() {
		return Optional.of(JarParams.class);
	}
}
