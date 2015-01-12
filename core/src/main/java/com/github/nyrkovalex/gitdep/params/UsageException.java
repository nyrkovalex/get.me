package com.github.nyrkovalex.gitdep.params;

public class UsageException extends Exception {
    private static final String USAGE = "" +
            "Usage:\n" +
            "gitdep <url> [, url, ...] [ -debug ]\n" +
            "\n" +
            "Parameters:\n" +
            "url - Url of a dependency repository. Should be in a format understandable by git\n" +
            "\n" +
            "Flags:\n" +
            "-debug - Enable debug output\n";

    @Override
    public String getMessage() {
        return USAGE;
    }
}
