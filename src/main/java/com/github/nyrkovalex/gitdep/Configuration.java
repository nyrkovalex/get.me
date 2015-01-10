package com.github.nyrkovalex.gitdep;

import com.github.nyrkovalex.gitdep.build.BuildExecutor;
import com.github.nyrkovalex.gitdep.build.mvn.MvnExecutor;
import com.github.nyrkovalex.gitdep.params.Parameters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Configuration {
    private final Set<String> urls;

    public Configuration(Parameters params) {
        this.urls = params.urls();
    }

    public Set<BuildExecutor> executors () {
        return new HashSet<>(Arrays.asList(
            new MvnExecutor()
        ));
    }

    public Set<String> urls() {
        return urls;
    }
}
