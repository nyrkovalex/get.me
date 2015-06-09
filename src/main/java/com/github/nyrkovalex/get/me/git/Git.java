package com.github.nyrkovalex.get.me.git;

public final class Git {

	private Git() {
		// Module
	}

	public interface Cloner {

		CloneCommand clone(String url);
	}

	public interface CloneCommand {
		CloneCommand enableOutput(boolean enable);
		CloneCommand branch(String branchName);
		void to(String path) throws Err;
	}

	public static Cloner cloner() {
		return GitCloneCommand::new;
	}

	static class Err extends Exception {

		Err(String url, String path, Throwable cause) {
			super(String.format("Failed to clone %s to %s, see cause for details", url, path), cause);
		}
	}
}

