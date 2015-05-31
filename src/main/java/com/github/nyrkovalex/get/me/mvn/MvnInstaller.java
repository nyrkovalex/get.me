package com.github.nyrkovalex.get.me.mvn;

import com.github.nyrkovalex.get.me.api.GetMe;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MvnInstaller implements GetMe.Installer<MvnParams> {

  final static List<String> DEFAULT_GOALS = Arrays.asList("clean", "install");
  private final Mvn mvn;

  public MvnInstaller() {
    this.mvn = Mvn.instance();
  }

  MvnInstaller(Mvn mvn) {
    this.mvn = mvn;
  }

  @Override
  public void install(String workingDir, MvnParams params) throws GetMe.Err {
    List<String> goals = Objects.isNull(params.goals) || params.goals.isEmpty()
        ? DEFAULT_GOALS
        : params.goals;
    mvn.run(goals).in(workingDir);
  }

  @Override
  public Class paramsClass() {
    return MvnParams.class;
  }

}
