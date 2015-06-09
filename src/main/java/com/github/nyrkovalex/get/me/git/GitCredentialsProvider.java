package com.github.nyrkovalex.get.me.git;

import org.eclipse.jgit.errors.UnsupportedCredentialItem;
import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;

import java.util.Map;
import java.util.function.Function;

class GitCredentialsProvider extends CredentialsProvider {

	private final Map<Class<?>, Function<CredentialItem, Boolean>> handlers;

	GitCredentialsProvider(Map<Class<?>, Function<CredentialItem, Boolean>> handlers) {
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
