package org.finos.fluxnova.bpm.test.util;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class ConstantsTest {

    @Test
    void testConstants() {
        assertEquals(80, Constants.DEFAULT_COVERAGE);
        assertEquals("coverageThreshold", Constants.COVERAGE_THRESHOLD_PROPERTY);
        assertEquals("ignoreCoverageFailure", Constants.IGNORE_COVERAGE_FAILURE_PROPERTY);
        assertEquals("skipCoverage", Constants.SKIP_COVERAGE_PROPERTY);
        assertEquals("skipTests", Constants.SKIP_TESTS);
    }
}
