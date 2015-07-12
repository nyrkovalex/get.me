package com.github.nyrkovalex.get.me.param;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Params {

	private final Set<RepoUrl> urls;
	private final boolean debug;

	private Params(boolean debug, Set<RepoUrl> urls) {
		this.debug = debug;
		this.urls = Collections.unmodifiableSet(urls);
	}

	public static Params from(String... args) throws WrongUsageException {
		ParamCollector collector = new ParamCollector(args.length);
		collector.offer(args);
		return collector.collect();
	}

	public Set<RepoUrl> getUrls() {
		return urls;
	}

	public boolean isDebug() {
		return debug;
	}

	private static final class ParamCollector {

		private boolean debug;
		private final Set<RepoUrl> urls;

		private ParamCollector(int size) {
			debug = false;
			urls = new HashSet<>(size);
		}

		void offer(String arg) throws WrongUsageException {
			switch (arg) {
			case "-debug":
				debug = true;
				return;
			}
			urls.add(RepoUrl.parse(arg));
		}

		void offer(String... args) throws WrongUsageException {
			for (String arg : args) {
				offer(arg);
			}
		}

		private Params collect() throws WrongUsageException {
			if (urls.isEmpty()) {
				throw new WrongUsageException();
			}
			return new Params(debug, urls);
		}
	}
}
