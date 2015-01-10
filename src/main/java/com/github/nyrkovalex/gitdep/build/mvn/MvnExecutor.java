package com.github.nyrkovalex.gitdep.build.mvn;

import com.github.nyrkovalex.gitdep.build.BuildExecutionException;
import com.github.nyrkovalex.gitdep.build.BuildExecutor;
import com.github.nyrkovalex.seed.core.Seed;

import java.util.logging.Logger;

public class MvnExecutor implements BuildExecutor {

    private static final Logger LOG = Logger.getLogger(MvnExecutor.class.getName());

    @Override
    public void execute(String path) throws BuildExecutionException {
        Mvn.run("clean", "install").in(path);
    }

    @Override
    public boolean canHandle(String path) {
        boolean canHandle = Seed.Files.exists(pomPath(path));
        LOG.fine(() -> String.format("Got request if can handle %s the answer is %s", path, canHandle));
        return canHandle;
    }

    private String pomPath(String path) {
        return path + "/" + Mvn.POM_XML_NAME;
    }

    @Override
    public String toString() {
        return "MvnExecutor";
    }
}
