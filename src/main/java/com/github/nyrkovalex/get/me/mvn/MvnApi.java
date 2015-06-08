package com.github.nyrkovalex.get.me.mvn;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;

class MvnApi {

	Invoker invoker() {
		return new DefaultInvoker();
	}

	InvocationRequest invocationRequest() {
		return new DefaultInvocationRequest();
	}

}
