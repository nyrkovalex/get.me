package com.github.nyrkovalex.get.me.build;

import java.util.Collections;
import java.util.List;

public class MvnBuilderParams {

  public final List<String> goals;

  MvnBuilderParams() {
    this.goals = Collections.emptyList();
  }

  MvnBuilderParams(List<String> goals) {
    this.goals = goals;
  }

}
