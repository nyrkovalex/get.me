package com.github.nyrkovalex.get.me.git;

import com.github.nyrkovalex.seed.Seed;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.NullProgressMonitor;
import org.eclipse.jgit.lib.TextProgressMonitor;

import java.io.File;
import java.util.Optional;
import java.util.logging.Logger;

class GitCloneCommand implements Git.CloneCommand {

	private static final Logger LOG = Seed.logger(GitCloneCommand.class);
	private final String url;
	private Optional<String> branchName = Optional.empty();
	private boolean enableOutput = false;

	public GitCloneCommand(String url) {
		this.url = url;
	}

	@Override
	public GitCloneCommand branch(String branchName) {
		this.branchName = Optional.of(branchName);
		return this;
	}

	@Override
	public GitCloneCommand enableOutput(boolean enable) {
		this.enableOutput = enable;
		return this;
	}

	@Override
	public void to(String path) throws Git.Err {
		LOG.info(() -> String.format("Cloning %s into %s...", url, path));
		CloneCommand cloner = org.eclipse.jgit.api.Git.cloneRepository()
				.setURI(url)
				.setCredentialsProvider(new GitCredentialsProvider(
						CredentialHandlers.handlerMap()
				))
				.setProgressMonitor(
						enableOutput
								? new TextProgressMonitor()
								: NullProgressMonitor.INSTANCE
				)
				.setDirectory(new File(path));
		branchName.ifPresent(cloner::setBranch);
		try {
			cloner.call();
		} catch (GitAPIException e) {
			throw new Git.Err(url, path, e);
		}
	}
}
