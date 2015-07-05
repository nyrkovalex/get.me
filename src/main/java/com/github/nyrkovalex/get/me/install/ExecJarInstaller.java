package com.github.nyrkovalex.get.me.install;

import com.github.nyrkovalex.get.me.api.GetMe;
import com.github.nyrkovalex.get.me.env.Envs;
import com.github.nyrkovalex.seed.Io;
import java.util.Objects;
import java.util.Optional;

public class ExecJarInstaller implements GetMe.Plugin<JarParams> {

	private final Io.Fs fs;
	private final Envs.Env env;

	public ExecJarInstaller() {
		this.fs = Io.fs();
		this.env = Envs.env();
	}

	ExecJarInstaller(Io.Fs fs, Envs.Env env) {
		this.fs = fs;
		this.env = env;
	}

	@Override
	public void exec(String workingDir, Optional<JarParams> params) throws GetMe.Err {
		JarParams jarParams = params.orElseThrow(() -> {
			return new GetMe.Err("`jar` parameter must be provided");
		});
		String targetPath = jarParams.jar;
		try {
			Io.File sourceFile = sourceJar(workingDir, targetPath);
			Io.File targetFile = targetFile(sourceFile.name());
			sourceFile.copyTo(targetFile);
		} catch (Io.Err err) {
			throw new GetMe.Err(String.format("Failed to copy %s to %s", targetPath, env.jarPath()), err);
		}
	}

	private Io.File targetFile(String sourceFile) throws Io.Err, GetMe.Err {
		String jarPath = env.jarPath();
		if (Objects.isNull(jarPath) || jarPath.isEmpty()) {
			throw new GetMe.Err("JARPATH environment variable is not set");
		}
		return fs.file(jarPath, sourceFile);
	}

	private Io.File sourceJar(String workingDir, String targetPath) throws GetMe.Err, Io.Err {
		if (Objects.isNull(targetPath) || targetPath.isEmpty()) {
			throw new GetMe.Err("No \"jar\" param provided");
		}
		Io.File sourceFile = fs.file(workingDir, targetPath);
		if (!sourceFile.exists()) {
			throw new GetMe.Err(sourceFile.path() + " does not exist");
		}
		return sourceFile;
	}

	@Override
	public Optional<Class<JarParams>> paramsClass() {
		return Optional.of(JarParams.class);
	}

}
