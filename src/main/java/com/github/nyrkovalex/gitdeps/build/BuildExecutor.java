package com.github.nyrkovalex.gitdeps.build;

public interface BuildExecutor {
    public void execute(String path) throws BuildExecutionException;
    public boolean canHandle(String path);
}
