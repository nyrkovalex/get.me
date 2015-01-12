package com.github.nyrkovalex.gitdep.conf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassloaderProvider {

    private static final String PLUGINS_DIR = "../plugins";
    private static final Logger LOG = Logger.getLogger(ClassloaderProvider.class.getName());

    public ClassLoader get() {
        List<URL> plugins = readDirectory(Paths.get(PLUGINS_DIR));
        plugins.forEach(p -> LOG.fine(() -> "Added " + p + " to classpath"));
        return new URLClassLoader(plugins.toArray(new URL[plugins.size()]));
    }

    // TODO: quick and dirty implementation
    private List<URL> readDirectory(Path directory) {
        List<URL> plugins = safeList(directory)
                .filter(f -> !Files.isDirectory(f))
                .map(f -> pathToUrl(f))
                .collect(Collectors.toList());
        List<URL> children = safeList(directory)
                .filter(f -> Files.isDirectory(f))
                .map(this::readDirectory)
                .reduce((t, u) -> {
                    List<URL> acc = new ArrayList<>(t.size() + u.size());
                    acc.addAll(t);
                    acc.addAll(u);
                    return acc;
                })
                .orElse(Collections.emptyList());
        plugins.addAll(children);
        return plugins;
    }

    private Stream<Path> safeList(Path directory) throws IllegalStateException {
        try {
            return Files.list(directory);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read plugin directory", ex);
        }
    }

    private static URL pathToUrl(Path p) throws AssertionError {
        try {
            return p.toUri().toURL();
        } catch (MalformedURLException ex) {
            throw new AssertionError("Should not happen");
        }
    }

}
