package com.github.nyrkovalex.get.me;

import com.github.nyrkovalex.seed.Tests;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

public class ParamsTest extends Tests.Expect {

  private String gitdepUrl;
  private String guavaUrl;

  @Before
  public void setUp() throws Exception {
    gitdepUrl = "git@github.com:nyrkovalex/gitdep.git";
    guavaUrl = "https://github.com/google/guava.git";
  }

  @Test
  public void testShouldSetRepositoryUrl() throws Exception {
    ParamsParsed params = ParamsParsed.from(gitdepUrl);
    expect(params.urls().contains(gitdepUrl)).toBe(true);
  }

  @Test
  public void testShouldSetMultipleRepositoryUrls() throws Exception {
    ParamsParsed params = ParamsParsed.from(gitdepUrl, guavaUrl);
    expect(params.urls().containsAll(Arrays.asList(gitdepUrl, guavaUrl))).toBe(true);
  }

  @Test
  public void testShouldEnableDebugMode() throws Exception {
    ParamsParsed parameters = ParamsParsed.from(gitdepUrl, "-debug");
    expect(parameters.debug()).toBe(true);
  }

  @Test(expected = Params.Err.class)
  public void testShouldThrowWhenNoArgumentsArePassed() throws Exception {
    ParamsParsed.from();
  }
}
