package com.github.nyrkovalex.gitdep.git;

import com.github.nyrkovalex.gitdep.UnitTest;
import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.URIish;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CredentialsProviderTest extends UnitTest {

    @Mock private Function<CredentialItem, Boolean> function;
    @Mock private URIish urIish;

    private CredentialItem item;

    private Map<Class<?>, Function<CredentialItem, Boolean>> handlers = new HashMap<>();
    private Git.CredentialsProvider provider;

    @Before
    public void setUp() throws Exception {
        item = new CredentialItem.YesNoType("test");
        handlers.put(CredentialItem.YesNoType.class, function);
        provider = new Git.CredentialsProvider(handlers);
    }

    @Test
    public void testShouldPickHandlerByClass() throws Exception {
        when(function.apply(item)).thenReturn(true);
        provider.get(urIish, item);
        verify(function).apply(item);
    }

    @Test
    public void testShouldSupportClassesFromHandlerMap() throws Exception {
        assertThat(provider.supports(item), is(true));
    }

    @Test
    public void testShouldNotSupportClassesNotFromHandlerMap() throws Exception {
        assertThat(provider.supports(new CredentialItem.Username()), is(false));
    }

    @Test
    public void testShouldBeInteractove() throws Exception {
        assertThat(provider.isInteractive(), is(true));

    }
}
