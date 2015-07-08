package com.github.nyrkovalex.get.me.param;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ParamsParsedTest {

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
		assertTrue(params.getUrls().contains(RepoUrl.parse(gitdepUrl)));
	}

	@Test
	public void testShouldSetMultipleRepositoryUrls() throws Exception {
		ParamsParsed params = ParamsParsed.from(gitdepUrl, guavaUrl);
		List<RepoUrl> target = Arrays.asList(RepoUrl.parse(gitdepUrl), RepoUrl.parse(guavaUrl));
		assertTrue(params.getUrls().containsAll(target));
	}

	@Test
	public void testShouldEnableDebugMode() throws Exception {
		ParamsParsed parameters = ParamsParsed.from(gitdepUrl, "-debug");
		assertTrue(parameters.isDebug());
	}

	@Test(expected = Params.Err.class)
	public void testShouldThrowWhenNoArgumentsArePassed() throws Exception {
		ParamsParsed.from();
	}

	@Test
	public void testShouldParseDebugAndOneUrl() throws Exception {
		String url = "https://github.com/my/repo";
		ParamsParsed params = ParamsParsed.from("-debug", url);
		Set<RepoUrl> urls = new HashSet<>(1);
		urls.add(RepoUrl.parse(url));
		assertThat(params.getUrls(), is(urls));
		assertThat(params.isDebug(), is(true));
	}

	@Test
	public void testShouldParseTwoUrls() throws Exception {
		String url1 = "https://github.com/my/repo";
		String url2 = "https://github.com/my/repo::branch-1";
		ParamsParsed params = ParamsParsed.from(url1, url2);
		Set<RepoUrl> urls = new HashSet<>(2);
		urls.add(RepoUrl.parse(url1));
		urls.add(RepoUrl.parse(url2));
		assertThat(params.getUrls(), is(urls));
		assertThat(params.isDebug(), is(false));
	}
}
