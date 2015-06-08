package com.github.nyrkovalex.get.me.mvn;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MvnParams {

	public final List<String> goals;

	MvnParams() {
		this.goals = Collections.emptyList();
	}

	MvnParams(List<String> goals) {
		this.goals = goals;
	}

	public boolean hasGoals() {
		return !(Objects.isNull(goals) || goals.isEmpty());
	}
}
