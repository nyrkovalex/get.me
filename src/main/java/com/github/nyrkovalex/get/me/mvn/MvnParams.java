package com.github.nyrkovalex.get.me.mvn;

import java.util.Collections;
import java.util.List;

public class MvnParams {

	public final List<String> goals;

	MvnParams() {
		this.goals = Collections.emptyList();
	}

	MvnParams(List<String> goals) {
		this.goals = goals;
	}

}
