package com.github.nyrkovalex.get.me.mvn;

import java.util.Arrays;
import java.util.List;

public class MvnInstaller extends MvnPlugin {

	final static List<String> DEFAULT_GOALS = Arrays.asList("clean", "exec");

	public MvnInstaller() {
		super(Mvn.instance(), DEFAULT_GOALS);
	}

	MvnInstaller(Mvn mvn) {
		super(mvn, DEFAULT_GOALS);
	}
}
