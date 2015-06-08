package com.github.nyrkovalex.get.me.install;

import com.github.nyrkovalex.get.me.Envs;
import com.github.nyrkovalex.get.me.api.GetMe;
import com.github.nyrkovalex.seed.Io;
import com.github.nyrkovalex.seed.Tests;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ExecJarInstallerTest extends Tests.Expect {

	@Mock Io.Fs fs;
	@Mock Envs.Env env;
	@Mock Io.File sourceJar;
	@Mock Io.File targetFile;
	final String workingDir = "/tmp";
	@InjectMocks ExecJarInstaller installer;

	ExecJarParams params;

	@Before
	public void setUp() throws Exception {
		given(fs.file("/tmp", "myJar")).returns(sourceJar);
		given(sourceJar.exists()).returns(Boolean.TRUE);
		given(sourceJar.name()).returns("myJar");

		given(env.jarPath()).returns("jarPath");
		given(fs.file("jarPath", "myJar")).returns(targetFile);

		params = new ExecJarParams("myJar");
	}

	@Test(expected = GetMe.Err.class)
	public void testShouldThrowWhenNoJarParamFound() throws Exception {
		installer.install(workingDir, new ExecJarParams());
	}

	@Test(expected = GetMe.Err.class)
	public void testShouldThrowOnEmptyJarParam() throws Exception {
		installer.install(workingDir, new ExecJarParams(""));
	}

	@Test
	public void testShouldCopyExecutableJarToJarPath() throws Exception {
		installer.install(workingDir, params);
		expect(sourceJar).toHaveCall().copyTo(targetFile);
	}

	@Test(expected = GetMe.Err.class)
	public void testShouldThrowIfNoJarExists() throws Exception {
		given(sourceJar.exists()).returns(Boolean.FALSE);
		installer.install(workingDir, params);
	}

	@Test(expected = GetMe.Err.class)
	public void testShouldThrowIfJarPathIsNotSet() throws Exception {
		given(env.jarPath()).returns(null);
		installer.install(workingDir, params);
	}

}
