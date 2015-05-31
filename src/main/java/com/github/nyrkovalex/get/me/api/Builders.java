package com.github.nyrkovalex.get.me.api;

public final class Builders {

  private Builders() {
    // Module
  }

  public interface Builder<P> {

    void build(String path, P params) throws Err;

    Class<P> paramsClass();
  }

  public static class Err extends Exception {

    public Err() {
    }

    public Err(String message) {
      super(message);
    }

    public Err(String message, Exception cause) {
      super(message, cause);
    }
  }
}
