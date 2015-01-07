package com.github.nyrkovalex.gitdeps.fs;

import com.github.nyrkovalex.gitdeps.UnitTest;
import com.github.nyrkovalex.seed.core.Seed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TempDirectoryTest extends UnitTest {

    private TempDirectory tempDirectory;

    @Before
    public void setUp() throws Exception {
        tempDirectory = TempDirectory.create();
    }

    @After
    public void tearDown() throws Exception {
        // Cleanup just in case
        Path path = Paths.get(tempDirectory.path());
        if (Files.exists(path)) {
            Seed.Files.deleteWithContents(tempDirectory.path());
        }
    }

    @Test
    public void testShouldCreateTemporaryDir() throws Exception {
        assertThat(Seed.Files.exists(tempDirectory.path()));
    }

    @Test
    public void testShouldDeleteTempDir() throws Exception {
        tempDirectory.delete();
        assertThat(!Seed.Files.exists(tempDirectory.path()));
    }

    @Test
    public void testShouldDeleteTempDirWithFiles() throws Exception {
        Files.createFile(Paths.get(tempDirectory.path(), "/garbage"));
        tempDirectory.delete();
        assertThat(!Seed.Files.exists(tempDirectory.path()));
    }

    @Test
    public void testShouldWorkWithTryWithResources() throws Exception {
        String path;
        try (TempDirectory tmp = TempDirectory.create()) {
            path = tmp.path();
        }
        assertThat(!Seed.Files.exists(path));
    }
}
