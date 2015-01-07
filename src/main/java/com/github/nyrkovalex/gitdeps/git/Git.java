package com.github.nyrkovalex.gitdeps.git;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.UnsupportedCredentialItem;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.URIish;

import java.io.File;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

public final class Git {

    private static final Logger LOG = Logger.getLogger(Git.class.getName());

    private Git() {
    }

    public static Cloner clone(String url) {
        return new Cloner(url);
    }

    public static class Cloner {

        private final String url;

        public Cloner(String url) {
            this.url = url;
        }

        public void to(String path) throws CloneFailedException {
            LOG.fine(() -> String.format("Cloning %s into %s...", url, path));
            CloneCommand cloner = org.eclipse.jgit.api.Git.cloneRepository()
                    .setURI(url)
                    .setCredentialsProvider(new CredentialsProvider(
                            CredentialHandlers.handlerMap()))
                    .setProgressMonitor(new TextProgressMonitor())
                    .setDirectory(new File(path));
            try {
                cloner.call();
            } catch (GitAPIException e) {
                throw new CloneFailedException(url, path, e);
            }
        }
    }

    static class CredentialsProvider extends org.eclipse.jgit.transport.CredentialsProvider {

        private final Map<Class<?>, Function<CredentialItem, Boolean>> handlers;

        CredentialsProvider(Map<Class<?>, Function<CredentialItem, Boolean>> handlers) {
            this.handlers = handlers;
        }

        @Override
        public boolean isInteractive() {
            return true;
        }

        @Override
        public boolean supports(CredentialItem... items) {
            for (CredentialItem item : items) {
                if (!handlers.containsKey(item.getClass())) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean get(URIish uri, CredentialItem... items) throws UnsupportedCredentialItem {
            System.out.println("Credentials are required to clone " + uri.toString());
            for (CredentialItem item : items) {
                if (!handle(item)) {
                    return false;
                }
            }
            return true;
        }

        private boolean handle(CredentialItem item) {
            return handlers.get(item.getClass()).apply(item);
        }
    }

}
