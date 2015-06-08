package com.github.nyrkovalex.get.me.mvn;

import java.util.Arrays;
import java.util.List;

public class MvnBuilder extends MvnPlugin {

	static final List<String> DEFAULT_GOALS = Arrays.asList("clean", "package");

	public MvnBuilder() {
		super(Mvn.instance(), DEFAULT_GOALS);
	}

	MvnBuilder(Mvn mvn) {
		super(mvn, DEFAULT_GOALS);
	}
}
