package com.github.nyrkovalex.gitdep.conf;

import com.github.nyrkovalex.seed.core.Seed;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ExecutorsFile {
    private static final String EXECUTORS_FILENAME = "executors.xml";

    public ExecutorsDocument read() throws MalformedConfigurationException {
        try {
            return ExecutorsDocument.from(new FileInputStream(EXECUTORS_FILENAME));
        } catch (FileNotFoundException e) {
            throw new MalformedConfigurationException(e);
        }
    }

    public boolean exists() {
        return Seed.Files.exists(EXECUTORS_FILENAME);
    }

    public ExecutorsDocument createDefault() {
        ExecutorsDocument executorsDocument = ExecutorsDocument.defaultOne();
        try {
            executorsDocument.writeTo(new FileOutputStream(EXECUTORS_FILENAME));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Cannot write configuration to " + EXECUTORS_FILENAME, e);
        }
        return executorsDocument;
    }
}
