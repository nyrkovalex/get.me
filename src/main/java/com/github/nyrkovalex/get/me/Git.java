package com.github.nyrkovalex.get.me;

import com.github.nyrkovalex.seed.Seed;
import com.github.nyrkovalex.seed.Sys;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.UnsupportedCredentialItem;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;

final class Git {

  private Git() {
    // Module
  }

  public interface Cloner {

    CloneCommand clone(String url);
  }

  public interface CloneCommand {

    void to(String path) throws Err;
  }

  public static Cloner cloner() {
    return GitCloneCommand::new;
  }

  static class Err extends Exception {

    Err(String url, String path, Throwable cause) {
      super(String.format("Failed to clone %s to %s, see cause for details", url, path), cause);
    }
  }
}

class GitCloneCommand implements Git.CloneCommand {

  private static final Logger LOG = Seed.logger(GitCloneCommand.class);
  private final String url;

  public GitCloneCommand(String url) {
    this.url = url;
  }

  @Override
  public void to(String path) throws Git.Err {
    LOG.fine(() -> String.format("Cloning %s into %s...", url, path));
    CloneCommand cloner = org.eclipse.jgit.api.Git.cloneRepository()
        .setURI(url)
        .setCredentialsProvider(new GitCredentialsProvider(
                CredentialHandlers.handlerMap()))
        .setProgressMonitor(new TextProgressMonitor())
        .setDirectory(new File(path));
    try {
      cloner.call();
    } catch (GitAPIException e) {
      throw new Git.Err(url, path, e);
    }
  }
}

class CredentialHandlers {

  private static final Sys.Console CONSOLE = Sys.console();
  private static final StringHandler STRING_HANDLER = new StringHandler(CONSOLE);
  private static final CharArrayHandler CHAR_ARR_HANDLER = new CharArrayHandler(CONSOLE);
  private static final YesNoHandler YES_NO_HANDLER = new YesNoHandler(CONSOLE);
  private static final CredentialHandlers.InfoHandler INFO_HANDLER = new CredentialHandlers.InfoHandler();

  static Map<Class<?>, Function<CredentialItem, Boolean>> handlerMap() {
    Map<Class<?>, Function<CredentialItem, Boolean>> map = new HashMap<>();
    map.put(CredentialItem.StringType.class, STRING_HANDLER);
    map.put(CredentialItem.Username.class, STRING_HANDLER);
    map.put(CredentialItem.CharArrayType.class, CHAR_ARR_HANDLER);
    map.put(CredentialItem.Password.class, CHAR_ARR_HANDLER);
    map.put(CredentialItem.YesNoType.class, YES_NO_HANDLER);
    map.put(CredentialItem.InformationalMessage.class, INFO_HANDLER);
    return Collections.unmodifiableMap(map);
  }

  static class InfoHandler implements Function<CredentialItem, Boolean> {

    @Override
    public Boolean apply(CredentialItem credentialItem) {
      System.out.println(credentialItem.getPromptText());
      return true;
    }
  }

  static class StringHandler extends InputHandler {

    protected StringHandler(Sys.Console console) {
      super(console);
    }

    @Override
    public Boolean apply(CredentialItem credentialItem) {
      CredentialItem.StringType casted = (CredentialItem.StringType) credentialItem;
      casted.setValue(readInput(credentialItem));
      return true;
    }
  }

  static class CharArrayHandler extends InputHandler {

    protected CharArrayHandler(Sys.Console console) {
      super(console);
    }

    @Override
    public Boolean apply(CredentialItem credentialItem) {
      CredentialItem.CharArrayType casted = (CredentialItem.CharArrayType) credentialItem;
      casted.setValue(readInput(credentialItem).toCharArray());
      return true;
    }
  }

  static class YesNoHandler extends InputHandler {

    protected YesNoHandler(Sys.Console console) {
      super(console);
    }

    @Override
    public Boolean apply(CredentialItem credentialItem) {
      String input = readInput(credentialItem);
      CredentialItem.YesNoType casted = (CredentialItem.YesNoType) credentialItem;
      casted.setValue(input.toLowerCase().equals("y"));
      return true;
    }
  }

  static abstract class InputHandler implements Function<CredentialItem, Boolean> {

    private final Sys.Console console;

    protected InputHandler(Sys.Console console) {
      this.console = console;
    }

    protected String readInput(CredentialItem credentialItem) {
      String prompt = String.format("Please provide %s: ", credentialItem.getPromptText());
      return credentialItem.isValueSecure()
          ? console.readSecure(prompt)
          : console.read(prompt);
    }
  }
}

class GitCredentialsProvider extends CredentialsProvider {

  private final Map<Class<?>, Function<CredentialItem, Boolean>> handlers;

  GitCredentialsProvider(Map<Class<?>, Function<CredentialItem, Boolean>> handlers) {
    this.handlers = handlers;
  }

  @Override
  public boolean isInteractive() {
    return true;
  }

  @Override
  public boolean supports(CredentialItem... items) {
    for (CredentialItem item : items) {
      if (!handlers.containsKey(item.getClass())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean get(URIish uri, CredentialItem... items) throws UnsupportedCredentialItem {
    System.out.println("Credentials are required to clone " + uri.toString());
    for (CredentialItem item : items) {
      if (!handle(item)) {
        return false;
      }
    }
    return true;
  }

  private boolean handle(CredentialItem item) {
    return handlers.get(item.getClass()).apply(item);
  }

}
