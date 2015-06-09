package com.github.nyrkovalex.get.me.git;

import com.github.nyrkovalex.seed.Seed;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;

import java.io.File;
import java.util.logging.Logger;

class GitCloneCommand implements Git.CloneCommand {

	private static final Logger LOG = Seed.logger(GitCloneCommand.class);
	private final String url;

	public GitCloneCommand(String url) {
		this.url = url;
	}

	@Override
	public void to(String path) throws Git.Err {
		LOG.fine(() -> String.format("Cloning %s into %s...", url, path));
		CloneCommand cloner = org.eclipse.jgit.api.Git.cloneRepository()
				.setURI(url)
				.setCredentialsProvider(new GitCredentialsProvider(
						CredentialHandlers.handlerMap()))
				.setProgressMonitor(new TextProgressMonitor())
				.setDirectory(new File(path));
		try {
			cloner.call();
		} catch (GitAPIException e) {
			throw new Git.Err(url, path, e);
		}
	}
}
