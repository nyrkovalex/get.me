package com.github.nyrkovalex.get.me.build;

import com.github.nyrkovalex.get.me.api.Builders;
import com.github.nyrkovalex.seed.Seed;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class MvnBuilder implements Builders.Builder<MvnBuilderParams> {

  static final String POM_XML_NAME = "pom.xml";
  static final List<String> DEFAULT_GOALS = Arrays.asList("clean", "package");
  private final Mvn mvn;

  public MvnBuilder() {
    mvn = Mvn.instance();
  }

  MvnBuilder(Mvn mvn) {
    this.mvn = mvn;
  }

  @Override
  public void build(String path, MvnBuilderParams params) throws Builders.Err {
    mvn.run(params.goals.isEmpty() ? DEFAULT_GOALS : params.goals).in(path);
  }

  @Override
  public String toString() {
    return "MvnBuilder";
  }

  @Override
  public Class<MvnBuilderParams> paramsClass() {
    return MvnBuilderParams.class;
  }

}

class MvnApi {

  Invoker invoker() {
    return new DefaultInvoker();
  }

  InvocationRequest invocationRequest() {
    return new DefaultInvocationRequest();
  }
}

class Mvn {

  private static final Mvn INSTANCE = new Mvn();
  private static final MvnApi API = new MvnApi();

  public static Mvn instance() {
    return INSTANCE;
  }

  Runner run(List<String> targets) {
    return new Runner(API, targets);
  }

  static class Runner {

    private final static Logger LOG = Seed.logger(Runner.class);
    private final MvnApi api;

    private final List<String> goals;

    Runner(MvnApi api, List<String> goals) {
      this.api = api;
      this.goals = Collections.unmodifiableList(new ArrayList<>(goals));
    }

    public void in(String path) throws Builders.Err {
      LOG.fine(() -> String.format("running `mvn %s` in %s", Seed.Strings.join(" ", goals), path));
      InvocationRequest request = createInvocationRequest(path);
      Invoker invoker = createMvnInvoker(path);
      run(request, invoker);
      LOG.fine(() -> String.format("completed `mvn %s` in %s", Seed.Strings.join(" ", goals), path));
    }

    private void run(InvocationRequest request, Invoker invoker) throws Builders.Err {
      try {
        invoker.execute(request);
      } catch (MavenInvocationException e) {
        throw new Builders.Err("Maven execution failed", e);
      }
    }

    private Invoker createMvnInvoker(String path) {
      Invoker invoker = api.invoker();
      invoker.setWorkingDirectory(Paths.get(path).toFile());
      return invoker;
    }

    private InvocationRequest createInvocationRequest(String path) {
      InvocationRequest request = api.invocationRequest();
      request.setPomFile(Paths.get(path, MvnBuilder.POM_XML_NAME).toFile());
      request.setGoals(goals);
      return request;
    }
  }
}
