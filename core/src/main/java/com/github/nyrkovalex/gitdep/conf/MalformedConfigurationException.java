package com.github.nyrkovalex.gitdep.conf;

public class MalformedConfigurationException extends Exception {
    public MalformedConfigurationException(Throwable cause) {
        super("Failed to read executors.xml file", cause);
    }
}
