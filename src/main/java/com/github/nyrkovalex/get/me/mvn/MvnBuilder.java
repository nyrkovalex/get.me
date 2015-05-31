package com.github.nyrkovalex.get.me.mvn;

import com.github.nyrkovalex.get.me.api.GetMe;
import java.util.Arrays;
import java.util.List;

public class MvnBuilder implements GetMe.Builder<MvnParams> {

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
  public void build(String path, MvnParams params) throws GetMe.Err {
    mvn.run(params.goals.isEmpty() ? DEFAULT_GOALS : params.goals).in(path);
  }

  @Override
  public String toString() {
    return "MvnBuilder";
  }

  @Override
  public Class<MvnParams> paramsClass() {
    return MvnParams.class;
  }

}


