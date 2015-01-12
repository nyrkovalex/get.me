package com.github.nyrkovalex.gitdep.conf;

import com.github.nyrkovalex.gitdep.build.BuildExecutor;
import com.github.nyrkovalex.gitdep.params.Parameters;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class Configuration {

    private static final Logger LOG = Logger.getLogger(Configuration.class.getName());

    public static Configuration load(Parameters parameters, ClassloaderProvider classloaderProvider) throws MalformedConfigurationException {
        return new Configuration(parameters.urls(), parameters.executorsFile(), classloaderProvider);
    }

    private final Set<String> urls;
    private final ClassloaderProvider classloaderProvider;
    private final Set<BuildExecutor> executors;

    private Configuration(Set<String> urls, ExecutorsFile executorsFile, ClassloaderProvider classloaderProvider) throws MalformedConfigurationException {
        this.urls = urls;
        this.classloaderProvider = classloaderProvider;
        ExecutorsDocument executorsDocument = readExecutorsDocument(executorsFile);
        this.executors = loadExecutors(executorsDocument.executors());
    }

    private Set<BuildExecutor> loadExecutors(Set<String> executors) {
        Set<BuildExecutor> loaded = new HashSet<>(executors.size());
        executors.forEach(executorClass -> {
            try {
                BuildExecutor executor = loadExecutor(executorClass);
                loaded.add(executor);
            } catch (IllegalAccessException | InstantiationException |
                     ClassNotFoundException | IOException e) {
                LOG.warning(() -> "Failed to load executor " + executorClass);
                e.printStackTrace(System.err);
            }
        });
        return loaded;
    }

    private BuildExecutor loadExecutor(String executorClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException, IOException {
        ClassLoader classloader = classloaderProvider.get();
        Class<?> executorClass = classloader.loadClass(executorClassName);
        return (BuildExecutor) executorClass.newInstance();
    }

    private ExecutorsDocument readExecutorsDocument(ExecutorsFile executorsFile) throws MalformedConfigurationException {
        if (executorsFile.exists()) {
            return executorsFile.read();
        }
        return executorsFile.createDefault();
    }

    public Set<BuildExecutor> executors() {
        return Collections.unmodifiableSet(executors);
    }

    public Set<String> urls() {
        return Collections.unmodifiableSet(urls);
    }

}
