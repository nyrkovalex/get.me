package com.github.nyrkovalex.get.me.param;

import com.github.nyrkovalex.seed.Tests;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class ParamsTest extends Tests.Expect {

	private String gitdepUrl;
	private String guavaUrl;

	@Before
	public void setUp() throws Exception {
		gitdepUrl = "git@github.com:nyrkovalex/gitdep.git";
		guavaUrl = "https://github.com/google/guava.git";
	}

	@Test
	public void testShouldSetRepositoryUrl() throws Exception {
		ParamsParsed params = ParamsParsed.from(gitdepUrl);
		expect(params.getUrls().contains(gitdepUrl)).toBe(true);
	}

	@Test
	public void testShouldSetMultipleRepositoryUrls() throws Exception {
		ParamsParsed params = ParamsParsed.from(gitdepUrl, guavaUrl);
		expect(params.getUrls().containsAll(Arrays.asList(gitdepUrl, guavaUrl))).toBe(true);
	}

	@Test
	public void testShouldEnableDebugMode() throws Exception {
		ParamsParsed parameters = ParamsParsed.from(gitdepUrl, "-debug");
		expect(parameters.isDebug()).toBe(true);
	}

	@Test(expected = Params.Err.class)
	public void testShouldThrowWhenNoArgumentsArePassed() throws Exception {
		ParamsParsed.from();
	}
}
