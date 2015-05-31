package com.github.nyrkovalex.get.me.api;

public final class Installers {
  private Installers() {
    // Module
  }

  public interface Installer<P> {

    void install(String workingDir, P params) throws Err;
    Class<P> paramsClass();
  }

  public static class Err extends Exception {

    public Err() {
    }

    public Err(String message) {
      super(message);
    }

    public Err(String message, Throwable cause) {
      super(message, cause);
    }
  }
}


