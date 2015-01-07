package com.github.nyrkovalex.gitdeps.build.mvn;

import com.github.nyrkovalex.seed.core.Seed;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import com.github.nyrkovalex.gitdeps.build.BuildExecutionException;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Mvn {
    public static final String POM_XML_NAME = "pom.xml";
    private static final Logger LOG = Logger.getLogger(Mvn.class.getName());

    public static Runner run(List<String> targets) {
        return new Runner(targets);
    }

    public static Runner run(String... targets) {
        return new Runner(Arrays.asList(targets));
    }

    public static class Runner {
        private final List<String> targets;

        public Runner(List<String> targets) {
            this.targets = Collections.unmodifiableList(new ArrayList<>(targets));
        }

        public void in(String path) throws BuildExecutionException {
            LOG.fine(() -> String.format("running `mvn %s` in %s", Seed.Strings.join(" ", targets), path));
            InvocationRequest request = createInvocationRequest(path);
            Invoker invoker = createMvnInvoker(path);
            run(request, invoker);
            LOG.fine(() -> String.format("completed `mvn %s` in %s", Seed.Strings.join(" ", targets), path));
        }

        private void run(InvocationRequest request, Invoker invoker) throws BuildExecutionException {
            try {
                invoker.execute(request);
            } catch (MavenInvocationException e) {
                throw new BuildExecutionException("Maven execution failed", e);
            }
        }

        private Invoker createMvnInvoker(String path) {
            DefaultInvoker invoker = new DefaultInvoker();
            invoker.setWorkingDirectory(Paths.get(path).toFile());
            return invoker;
        }

        private InvocationRequest createInvocationRequest(String path) {
            InvocationRequest request = new DefaultInvocationRequest();
            request.setPomFile(Paths.get(path, POM_XML_NAME).toFile());
            request.setGoals(targets);
            return request;
        }
    }
}
