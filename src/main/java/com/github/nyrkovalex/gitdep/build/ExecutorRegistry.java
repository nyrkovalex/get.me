package com.github.nyrkovalex.gitdep.build;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class ExecutorRegistry {
    private static final ExecutorRegistry INSTANCE = new ExecutorRegistry();
    private static final Logger LOG = Logger.getLogger(ExecutorRegistry.class.getName());

    private ExecutorRegistry() {
        executors = new HashSet<>();
    }

    private final Set<BuildExecutor> executors;

    public void register(BuildExecutor executor) {
        LOG.fine("Registering " + executor);
        executors.add(executor);
    }

    public void offer(String path) throws BuildExecutionException {
        List<BuildExecutor> found = executors.stream()
                .filter((e) -> e.canHandle(path))
                .collect(Collectors.toList());
        BuildExecutor buildExecutor = retrieveExecutor(path, found);
        buildExecutor.execute(path);
    }

    private BuildExecutor retrieveExecutor(String path, List<BuildExecutor> found) throws BuildExecutionException {
        if (found.size() > 1) {
            throw new BuildExecutionException("More than one executor found for " + path);
        }
        if (found.size() == 0) {
            throw new BuildExecutionException("No executors found for path " + path);
        }
        return found.get(0);
    }

    public static ExecutorRegistry instance() {
        return INSTANCE;
    }
}
