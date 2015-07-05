package com.github.nyrkovalex.get.me.param;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class ParamsParsed implements Params.Parsed {

	private final Set<RepoUrl> urls;
	private final boolean debug;

	private ParamsParsed(boolean debug, Set<RepoUrl> urls) {
		this.debug = debug;
		this.urls = Collections.unmodifiableSet(urls);
	}

	public static ParamsParsed from(String... args) throws Params.Err {
		ParamCollector collector = new ParamCollector(args.length);
		collector.offer(args);
		return collector.collect();
	}

	@Override
	public Set<RepoUrl> getUrls() {
		return urls;
	}

	@Override
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

		void offer(String arg) throws Params.Err {
			switch (arg) {
			case "-debug":
				debug = true;
				return;
			}
			urls.add(RepoUrl.parse(arg));
		}

		void offer(String... args) throws Params.Err {
			for (String arg : args) {
				offer(arg);
			}
		}

		private ParamsParsed collect() throws Params.Err {
			if (urls.isEmpty()) {
				throw new Params.Err();
			}
			return new ParamsParsed(debug, urls);
		}
	}
}
