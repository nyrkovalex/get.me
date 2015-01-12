package com.github.nyrkovalex.gitdep;

import com.github.nyrkovalex.gitdep.build.ExecutorRegistry;
import com.github.nyrkovalex.gitdep.conf.ClassloaderProvider;
import com.github.nyrkovalex.gitdep.conf.Configuration;
import com.github.nyrkovalex.gitdep.conf.MalformedConfigurationException;
import com.github.nyrkovalex.gitdep.fs.TempDirectory;
import com.github.nyrkovalex.gitdep.git.Git;
import com.github.nyrkovalex.gitdep.params.Parameters;
import com.github.nyrkovalex.gitdep.params.UsageException;
import com.github.nyrkovalex.seed.core.Chain;
import com.github.nyrkovalex.seed.core.Flow;
import com.github.nyrkovalex.seed.core.Seed;
import java.io.IOException;
import java.util.logging.Formatter;
import java.util.logging.Logger;

public final class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String... args) throws Flow.InterruptedException {
        Flow.start(() -> args)
                .then(Main::parseArguments)
                .then(Main::initLogging)
                .then(Main::logRunning)
                .then(Main::configuration)
                .then(Main::registerExecutors)
                .then(Main::execute)
                .end();
    }

    private static Parameters parseArguments(String[] args) {
        try {
            return Parameters.from(args);
        } catch (UsageException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
        throw new AssertionError("Make compiler happy. Should never reach this");
    }

    private static Parameters initLogging(Parameters params) {
        Formatter formatter = params.debugEnabled()
                              ? new Seed.Logging.DetailedFormatter()
                              : new Seed.Logging.StdOutFormatter();
        Seed.Logging.init(params.debugEnabled(), Main.class, formatter);
        return params;
    }

    private static Configuration configuration(Parameters parameters) throws MalformedConfigurationException {
        return Configuration.load(parameters, new ClassloaderProvider());
    }

    private static Parameters logRunning(Parameters parameters) {
        LOG.fine(() -> "Running with debug enabled");
        return parameters;
    }

    private static Configuration registerExecutors(Configuration configuration) {
        configuration.executors().forEach((e) -> ExecutorRegistry.instance().register(e));
        return configuration;
    }

    private static Configuration execute(Configuration configuration) {
        try (TempDirectory tmpDir = TempDirectory.create()) {
            configuration.urls().forEach((url) -> chain(url, tmpDir.path()));
        } catch (IOException e) {
            LOG.warning(() -> "Failed to delete temp directory, see trace for details");
            e.printStackTrace(System.err);
        }
        return configuration;
    }

    private static void chain(String url, String path) {
        try {
            Chain.start(() -> Git.clone(url).to(path))
                    .then(() -> ExecutorRegistry.instance().offer(path))
                    .then(() -> LOG.info(() -> "Successfully built " + url))
                    .end();
        } catch (Chain.BrokenException e) {
            LOG.severe(() -> "Failed to process " + url);
            e.printStackTrace(System.err);
        }
    }
}
