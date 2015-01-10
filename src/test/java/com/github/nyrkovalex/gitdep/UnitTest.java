package com.github.nyrkovalex.gitdep;

import com.github.nyrkovalex.seed.test.Seed;
import org.junit.Before;

public class UnitTest extends Seed.Test {
    @Before
    public void initLogging() throws Exception {
        com.github.nyrkovalex.seed.core.Seed.Logging.init(true, UnitTest.class);
    }
}
