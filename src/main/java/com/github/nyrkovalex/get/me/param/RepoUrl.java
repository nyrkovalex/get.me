package com.github.nyrkovalex.get.me.param;

import java.util.Objects;
import java.util.Optional;

public class RepoUrl {

	public final static String BRANCH_SEPARATOR = "::";
	private final String url;
	private final Optional<String> branch;

	private RepoUrl(String url) {
		this.url = url;
		this.branch = Optional.empty();
	}

	private RepoUrl(String url, String branch) {
		this.url = url;
		this.branch = Optional.of(branch);
	}

	public static RepoUrl parse(String urlString) throws Params.Err {
		String[] urlAndBranch = urlString.split(BRANCH_SEPARATOR);
		if (urlAndBranch.length == 1) {
			return new RepoUrl(urlString);
		} else if (urlAndBranch.length == 2) {
			return new RepoUrl(urlAndBranch[0], urlAndBranch[1]);
		}
		throw new Params.Err("Malformed url");
	}

	public String getUrl() {
		return url;
	}

	public Optional<String> getBranch() {
		return branch;
	}

	@Override
	public int hashCode() {
		return Objects.hash(url, branch);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final RepoUrl other = (RepoUrl) obj;
		return Objects.equals(this.branch, other.branch)
				&& Objects.equals(this.url, other.url);
	}




}
