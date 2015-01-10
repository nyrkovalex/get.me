package com.github.nyrkovalex.gitdep.fs;


import com.github.nyrkovalex.seed.core.Seed;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public class TempDirectory implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(TempDirectory.class.getName());
    private final Path dir;

    private TempDirectory() throws IOException {
        dir = Files.createTempDirectory("");
        LOG.fine(() -> String.format("Created temp directory at %s", path()));
    }

    public static TempDirectory create() throws IOException {
        return new TempDirectory();
    }

    public String path() {
        return dir.toString();
    }

    public void delete() throws IOException {
        try {
            Seed.Files.deleteWithContents(path());
        } catch (RuntimeException e) {
            throw new IOException(e.getCause());
        }
    }

    @Override
    public void close() throws IOException {
        delete();
    }
}
