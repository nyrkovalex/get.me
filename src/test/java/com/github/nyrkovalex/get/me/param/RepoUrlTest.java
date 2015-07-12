package com.github.nyrkovalex.get.me.param;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class RepoUrlTest {

	@Test
	public void testShouldReadPlainUrl() throws Exception {
		RepoUrl read = RepoUrl.parse("https://github.com/me/repo");
		assertThat(read.getUrl(), is("https://github.com/me/repo"));
		assertThat(read.getBranch(), is(Optional.empty()));
	}

	@Test
	public void testShouldReadUrlWithBranch() throws Exception {
		RepoUrl read = RepoUrl.parse("https://github.com/me/repo::branch-1");
		assertThat(read.getUrl(), is("https://github.com/me/repo"));
		assertThat(read.getBranch(), is(Optional.of("branch-1")));
	}

	@Test(expected = WrongUsageException.class)
	public void testShouldThrowOnIllegalInput() throws Exception {
		RepoUrl.parse("https://github.com/me/repo::branch-1::branch-2");
	}

	@Test
	public void testShouldCompareSameUrlsWithBranch() throws Exception {
		RepoUrl first = RepoUrl.parse("https://github.com/me/repo::branch-1");
		RepoUrl second = RepoUrl.parse("https://github.com/me/repo::branch-1");
		assertThat(first.equals(second), is(true));
		assertThat(first.hashCode(), is(second.hashCode()));
	}

	@Test
	public void testShouldCompareSameUrlsWithoutBranch() throws Exception {
		RepoUrl first = RepoUrl.parse("https://github.com/me/repo");
		RepoUrl second = RepoUrl.parse("https://github.com/me/repo");
		assertThat(first.equals(second), is(true));
		assertThat(first.hashCode(), is(second.hashCode()));
	}

	@Test
	public void testShouldCompareDifferentUrls() throws Exception {
		RepoUrl first = RepoUrl.parse("https://github.com/me/repo");
		RepoUrl second = RepoUrl.parse("https://github.com/me/repo2");
		assertThat(first.equals(second), is(false));
		assertThat(first.hashCode(), is(not(second.hashCode())));
	}

	@Test
	public void testShouldCompareSameUrlsWithDifferentBranch() throws Exception {
		RepoUrl first = RepoUrl.parse("https://github.com/me/repo::branch-2");
		RepoUrl second = RepoUrl.parse("https://github.com/me/repo::branch-1");
		assertThat(first.equals(second), is(false));
		assertThat(first.hashCode(), is(not(second.hashCode())));
	}
}
