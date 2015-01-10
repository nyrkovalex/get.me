package com.github.nyrkovalex.gitdep.params;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Parameters {

    private final Set<String> urls;
    private final boolean debug;

    public Parameters(boolean debug, Set<String> urls) {
        this.debug = debug;
        this.urls = Collections.unmodifiableSet(urls);
    }

    public static Parameters from(String... args) throws UsageException {
        ParamCollector collector = new ParamCollector(args.length);
        collector.offer(args);
        collector.check();
        return new Parameters(collector.debug, collector.urls);
    }

    public Set<String> urls() {
        return urls;
    }

    public boolean debugEnabled() {
        return debug;
    }

    private static final class ParamCollector {
        private boolean debug;
        private Set<String> urls;

        public ParamCollector(int size) {
            debug = false;
            urls = new HashSet<>(size);
        }

        void offer(String arg) {
            switch (arg) {
            case "-debug":
                debug = true;
                return;
            }
            urls.add(arg);
        }

        public void offer(String... args) {
            for (String arg : args) {
                offer(arg);
            }
        }

        public void check() throws UsageException {
            if (urls.size() == 0) {
                throw new UsageException();
            }
        }
    }
}
