package com.github.nyrkovalex.get.me.param;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ParamsTest {

	private String gitdepUrl;
	private String guavaUrl;

	@Before
	public void setUp() throws Exception {
		gitdepUrl = "git@github.com:nyrkovalex/gitdep.git";
		guavaUrl = "https://github.com/google/guava.git";
	}

	@Test
	public void testShouldSetRepositoryUrl() throws Exception {
		Params params = Params.from(gitdepUrl);
		assertTrue(params.getUrls().contains(RepoUrl.parse(gitdepUrl)));
	}

	@Test
	public void testShouldSetMultipleRepositoryUrls() throws Exception {
		Params params = Params.from(gitdepUrl, guavaUrl);
		List<RepoUrl> target = Arrays.asList(RepoUrl.parse(gitdepUrl), RepoUrl.parse(guavaUrl));
		assertTrue(params.getUrls().containsAll(target));
	}

	@Test
	public void testShouldEnableDebugMode() throws Exception {
		Params parameters = Params.from(gitdepUrl, "-debug");
		assertTrue(parameters.isDebug());
	}

	@Test(expected = WrongUsageException.class)
	public void testShouldThrowWhenNoArgumentsArePassed() throws Exception {
		Params.from();
	}

	@Test
	public void testShouldParseDebugAndOneUrl() throws Exception {
		String url = "https://github.com/my/repo";
		Params params = Params.from("-debug", url);
		Set<RepoUrl> urls = new HashSet<>(1);
		urls.add(RepoUrl.parse(url));
		assertThat(params.getUrls(), is(urls));
		assertThat(params.isDebug(), is(true));
	}

	@Test
	public void testShouldParseTwoUrls() throws Exception {
		String url1 = "https://github.com/my/repo";
		String url2 = "https://github.com/my/repo::branch-1";
		Params params = Params.from(url1, url2);
		Set<RepoUrl> urls = new HashSet<>(2);
		urls.add(RepoUrl.parse(url1));
		urls.add(RepoUrl.parse(url2));
		assertThat(params.getUrls(), is(urls));
		assertThat(params.isDebug(), is(false));
	}
}
