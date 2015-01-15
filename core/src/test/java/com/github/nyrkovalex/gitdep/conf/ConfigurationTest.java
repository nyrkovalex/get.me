package com.github.nyrkovalex.gitdep.conf;

import com.github.nyrkovalex.gitdep.UnitTest;
import com.github.nyrkovalex.gitdep.build.BuildExecutionException;
import com.github.nyrkovalex.gitdep.build.BuildExecutor;
import com.github.nyrkovalex.gitdep.params.Parameters;
import com.github.nyrkovalex.seed.core.Seed;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConfigurationTest extends UnitTest {
    @Mock private Parameters params;
    @Mock private ExecutorsFile executorsFile;
    @Mock private ExecutorsDocument document;
    @Mock private Seed.ClassLoaderProvider classloaderProvider;

    @Before
    public void setUp() throws Exception {
        when(classloaderProvider.get()).thenReturn(getClass().getClassLoader());
        when(params.executorsFile()).thenReturn(executorsFile);
        when(executorsFile.createDefault()).thenReturn(document);
        when(executorsFile.read()).thenReturn(document);
        when(document.executors()).thenReturn(new HashSet<>(Arrays.asList(DummyExecutor.class.getName())));
    }

    @Test
    public void testShouldReadExistingConfig() throws Exception {
        when(executorsFile.exists()).thenReturn(true);
        Configuration.load(params, classloaderProvider);
        verify(executorsFile).read();
    }

    @Test
    public void testShouldCreateDefaultConfig() throws Exception {
        when(executorsFile.exists()).thenReturn(false);
        Configuration.load(params, classloaderProvider);
        verify(executorsFile).createDefault();
    }

    @Test
    public void testShouldLoadExecutorByClassName() throws Exception {
        Configuration cfg = Configuration.load(params, classloaderProvider);
        assertThat(cfg.executors().iterator().next(), is(instanceOf(DummyExecutor.class)));
    }

    public static class DummyExecutor implements BuildExecutor {

        @Override
        public void execute(String path) throws BuildExecutionException {

        }

        @Override
        public boolean canHandle(String path) {
            return false;
        }
    }
}
