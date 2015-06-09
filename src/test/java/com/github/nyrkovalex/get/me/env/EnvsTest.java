package com.github.nyrkovalex.get.me.env;

import com.github.nyrkovalex.seed.Sys;
import com.github.nyrkovalex.seed.Tests;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(Enclosed.class)
public class EnvsTest {

	public static final class EnvsEnvTest extends Tests.Expect {

		@Mock Sys.Env seedEnv;
		private Envs.Env env;

		@Before
		public void setUp() throws Exception {
			env = new EnvsEnv(seedEnv);
		}

		@Test
		public void testShouldGetCwd() throws Exception {
			given(seedEnv.cwd()).returns("foo");
			expect(env.cwd()).toBe("foo");
		}

		@Test
		public void testShouldReturnPluginsHome() throws Exception {
			given(seedEnv.userHome()).returns("user");
			expect(env.pluginsHome()).toBe("user" + EnvsEnv.GETME_HOME + EnvsEnv.PLUGINS_HOME);
		}

		@Test
		public void testShouldReturnGetMeHome() throws Exception {
			given(seedEnv.userHome()).returns("user");
			expect(env.getMeHome()).toBe("user" + EnvsEnv.GETME_HOME);
		}

		@Test
		public void testShouldReturnDescriptorFileName() throws Exception {
			expect(env.descriptorFileName()).toBe(EnvsEnv.DESCRIPTOR_FILENAME);
		}

		@Test
		public void testShouldReturnJarPathVariable() throws Exception {
			given(seedEnv.read("JARPATH")).returns("/my/jarpath");
			expect(env.jarPath()).toBe("/my/jarpath");
		}
	}

}
