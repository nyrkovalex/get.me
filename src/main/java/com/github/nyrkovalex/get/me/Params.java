package com.github.nyrkovalex.get.me;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Params {

  private Params() {
    // Module
  }

  public interface Parsed {
    Set<String> urls();
    boolean debug();
  }

  public static Parsed parse(String[] args) throws Err {
    return ParamsParsed.from(args);
  }

  public static class Err extends Exception {

    private static final String USAGE = ""
        + "Usage:\n"
        + "gitdep <url> [, url, ...] [ -debug ]\n"
        + "\n"
        + "Parameters:\n"
        + "url - Url of a dependency repository. Should be in a format understandable by git\n"
        + "\n"
        + "Flags:\n"
        + "-debug - Enable debug output\n";

    @Override
    public String getMessage() {
      return USAGE;
    }
  }
}

class ParamsParsed implements Params.Parsed {

  private final Set<String> urls;
  private final boolean debug;

  private ParamsParsed(boolean debug, Set<String> urls) {
    this.debug = debug;
    this.urls = Collections.unmodifiableSet(urls);
  }

  public static ParamsParsed from(String... args) throws Params.Err {
    ParamCollector collector = new ParamCollector(args.length);
    collector.offer(args);
    return collector.collect();
  }

  @Override
  public Set<String> urls() {
    return urls;
  }

  @Override
  public boolean debug() {
    return debug;
  }

  private static final class ParamCollector {

    private boolean debug;
    private final Set<String> urls;

    private ParamCollector(int size) {
      debug = false;
      urls = new HashSet<>(size);
    }

    void offer(String arg) {
      switch (arg) {
      case "-debug":
        debug = true;
        return;
      }
      urls.add(arg);
    }

    void offer(String... args) {
      for (String arg : args) {
        offer(arg);
      }
    }

    private ParamsParsed collect() throws Params.Err {
      if (urls.isEmpty()) {
        throw new Params.Err();
      }
      return new ParamsParsed(debug, urls);
    }
  }
}
