package com.github.nyrkovalex.get.me;

import com.github.nyrkovalex.get.me.api.GetMe;
import com.github.nyrkovalex.get.me.mvn.MvnBuilder;
import com.github.nyrkovalex.seed.Plugins;
import com.github.nyrkovalex.seed.Tests;
import static com.github.nyrkovalex.seed.Tests.Expect.expect;
import static com.github.nyrkovalex.seed.Tests.Expect.given;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class RegistriesTest extends Tests.Expect {

  @Mock Plugins.Repo repo;
  @Mock GetMe.Builder externalBuilder;
  Registries.Registry<GetMe.Builder> registry;

  @Before
  public void setUp() throws Exception {
    registry = Registries.registry(repo, GetMe.Builder.class, new MvnBuilder());
  }

  @Test
  public void testShouldReturnDefaultBuilder() throws Exception {
    GetMe.Builder found = registry.forName(MvnBuilder.class.getCanonicalName());
    expect(found.getClass().equals(MvnBuilder.class)).toBe(true);
  }

  @Test
  public void testShouldLoadBuilderFromRepo() throws Exception {
    given(repo.instanceOf("ExternalBuilder", GetMe.Builder.class)).returns(externalBuilder);
    expect(registry.forName("ExternalBuilder")).toBe(externalBuilder);
  }

  @Test(expected = Registries.Err.class)
  public void testShouldThrowWhenNoExecutorCanBeLoaded() throws Exception {
    given(repo.instanceOf("ExternalBuilder", GetMe.Builder.class)).failsWith(Plugins.Err.class);
    registry.forName("ExternalBuilder");
  }
}
