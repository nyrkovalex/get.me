package com.github.nyrkovalex.get.me.param;

import java.util.HashSet;
import java.util.Set;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class ParamsParsedTest {

	@Test
	public void testShouldParseDebugAndOneUrl() throws Exception {
		String url = "https://github.com/my/repo";
		ParamsParsed params = ParamsParsed.from("-debug", url);
		assertThat(params.isDebug(), is(true));
		Set<RepoUrl> urls = new HashSet<>(1);
		urls.add(RepoUrl.parse(url));
		assertThat(params.getUrls(), is(urls));
	}

	@Test
	public void testShouldParseTwoUrls() throws Exception {
		String url1 = "https://github.com/my/repo";
		String url2 = "https://github.com/my/repo::branch-1";
		ParamsParsed params = ParamsParsed.from(url1, url2);
		assertThat(params.isDebug(), is(false));
		Set<RepoUrl> urls = new HashSet<>(2);
		urls.add(RepoUrl.parse(url1));
		urls.add(RepoUrl.parse(url2));
		assertThat(params.getUrls(), is(urls));
	}
}
