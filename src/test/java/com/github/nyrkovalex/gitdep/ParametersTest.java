package com.github.nyrkovalex.gitdep;

import com.github.nyrkovalex.gitdep.params.Parameters;
import com.github.nyrkovalex.gitdep.params.UsageException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;

public class ParametersTest extends UnitTest {

    private String gitdepUrl;
    private String guavaUrl;

    @Before
    public void setUp() throws Exception {
        gitdepUrl = "git@github.com:nyrkovalex/gitdep.git";
        guavaUrl = "https://github.com/google/guava.git";
    }

    @Test
    public void testShouldSetRepositoryUrl() throws Exception {
        Parameters params = Parameters.from(gitdepUrl);
        assertThat(params.urls().contains(gitdepUrl), is(true));
    }

    @Test
    public void testShouldSetMultipleRepositoryUrls() throws Exception {
        Parameters params = Parameters.from(gitdepUrl, guavaUrl);
        assertThat(params.urls().containsAll(Arrays.asList(gitdepUrl, guavaUrl)), is(true));
    }

    @Test
    public void testShouldEnableDebugMode() throws Exception {
        Parameters parameters = Parameters.from(gitdepUrl, "-debug");
        assertThat(parameters.debugEnabled(), is(true));
    }

    @Test(expected = UsageException.class)
    public void testShouldThrowWhenNoArgumentsArePassed() throws Exception {
        Parameters.from();
    }
}
