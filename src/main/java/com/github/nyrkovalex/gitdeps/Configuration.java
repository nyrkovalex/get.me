package com.github.nyrkovalex.gitdeps;

import com.github.nyrkovalex.gitdeps.build.BuildExecutor;
import com.github.nyrkovalex.gitdeps.build.mvn.MvnExecutor;
import com.github.nyrkovalex.gitdeps.params.Parameters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Configuration {
    private final boolean debugEnabled;
    private final Set<String> urls;

    public Configuration(Parameters params) {
        this.debugEnabled = params.debugEnabled();
        this.urls = params.urls();
    }

    public Set<BuildExecutor> executors () {
        return new HashSet<>(Arrays.asList(
            new MvnExecutor()
        ));
    }

    public boolean debugEnabled() {
        return debugEnabled;
    }

    public Set<String> urls() {
        return urls;
    }
}
