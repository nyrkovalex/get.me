package com.github.nyrkovalex.get.me.install;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.github.nyrkovalex.get.me.api.GetMe;
import com.github.nyrkovalex.get.me.env.Envs;
import com.github.nyrkovalex.get.me.install.JarParams;
import com.github.nyrkovalex.get.me.install.PluginInstaller;
import com.github.nyrkovalex.seed.Io;


public class PluginInstallerTest {
	
	@Mock private Io.File jarFile;
	@Mock private Io.Fs fs;
	@Mock private Io.Dir pluginsDir;
	@Mock private Envs.Env env;

	private final JarParams params = new JarParams("plugin.jar");
	private PluginInstaller installer;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		installer = new PluginInstaller(env, fs);
	}
	
	
	@Test(expected = GetMe.Err.class)
	public void testShouldThrowWhenNoJarParamsPresent() throws Exception {
		installer.exec("/dev/null", Optional.empty());
	}
	
	@Test
	public void testShouldCopyJarToPluginsDir() throws Exception {
		when(fs.file("/dev/null", "plugin.jar")).thenReturn(jarFile);
		when(env.pluginsHome()).thenReturn("/home/me/get.me/plugins");
		when(fs.dir("/home/me/get.me/plugins")).thenReturn(pluginsDir);
		installer.exec("/dev/null", Optional.of(params));
		verify(jarFile).copyTo(pluginsDir);
	}
	
	@Test(expected = GetMe.Err.class)
	public void testShouldRethrowIoError() throws Exception {
		when(fs.file("/dev/null", "plugin.jar")).thenThrow(Io.Err.class);
		installer.exec("/dev/null", Optional.of(params));
	}
}