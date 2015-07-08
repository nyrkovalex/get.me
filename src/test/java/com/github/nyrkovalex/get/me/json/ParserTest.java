package com.github.nyrkovalex.get.me.json;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;
import com.gtihub.nyrkovalex.seed.nio.Fs;

public class ParserTest {
	
	private static class MvnParams {
		public final List<String> goals = Collections.emptyList();
	}
	
	private static class JarParams {
		public final String jar = null;
	}

	private static final String JSON = "[\n" +
			"  {\n" +
			"    \"class\": \"com.github.nyrkovalex.get.me.mvn.MvnBuilder\",\n" +
			"    \"params\": {\n" +
			"      \"goals\": [ \"clean\", \"package\" ]\n" +
			"    }\n" +
			"  },\n" +
			"  {\n" +
			"    \"class\": \"com.github.nyrkovalex.get.me.exec.ExecJarInstaller\",\n" +
			"    \"params\": {\n" +
			"      \"jar\": \"target/get.me.jar\"\n" +
			"    }\n" +
			"  }\n" +
			"]\n";

	@Mock Path file;
	@Mock Fs fs;
	List<Jsons.Description> parsed;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(fs.newBufferedReader(file)).thenReturn(new BufferedReader(new StringReader(JSON)));
		Parser parser = new Parser(new Gson(), fs);
		parsed = parser.parse(file);
	}

	@Test
	public void testShouldParseBuilder() throws Exception {
		assertThat(parsed.get(0).className(), is("com.github.nyrkovalex.get.me.mvn.MvnBuilder"));
	}

	@Test
	public void testShouldParseInstaller() throws Exception {
		assertThat(parsed.get(1).className(), is("com.github.nyrkovalex.get.me.exec.ExecJarInstaller"));
	}

	@Test
	public void testShouldParseBulderParams() throws Exception {
		MvnParams params = parsed.get(0).params(Optional.of(MvnParams.class)).get();
		assertThat(params.goals.get(0), is("clean"));
		assertThat(params.goals.get(1), is("package"));
	}

	@Test
	public void testShouldParseInstallerParams() throws Exception {
		JarParams params = parsed.get(1).params(Optional.of(JarParams.class)).get();
		assertThat(params.jar, is("target/get.me.jar"));
	}
}
