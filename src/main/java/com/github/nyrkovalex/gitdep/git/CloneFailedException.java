package com.github.nyrkovalex.gitdep.git;

public class CloneFailedException extends Exception {
    public CloneFailedException(String url, String path, Throwable cause) {
        super(String.format("Failed to clone %s to %s, see cause for details", url, path), cause);
    }
}
