package org.finos.fluxnova.bpm.test.coverage;

import org.finos.fluxnova.bpm.test.util.SystemWrapper;

import static org.finos.fluxnova.bpm.test.util.Constants.*;

public class CoverageProperties {

    private CoverageProperties() {}

    public static double getThresholdForProcessCoverageConfiguration() {
        double threshold = getThreshold();
        return threshold / 100;
    }

    public static double getThreshold() {
        String threshold = SystemWrapper.getProperty(COVERAGE_THRESHOLD_PROPERTY);
        if (threshold != null) {
            return Double.parseDouble(threshold);
        }
        return DEFAULT_COVERAGE;
    }

    public static boolean getIgnoreCoverageFailure() {
        String ignoreCoverageFailure = SystemWrapper.getProperty(IGNORE_COVERAGE_FAILURE_PROPERTY);
        return Boolean.parseBoolean(ignoreCoverageFailure);
    }

    public static boolean getSkipCoverage() {
        String skipCoverage = SystemWrapper.getProperty(SKIP_COVERAGE_PROPERTY);
        String skipTests = SystemWrapper.getProperty(SKIP_TESTS);
        return Boolean.parseBoolean(skipCoverage) || Boolean.parseBoolean(skipTests);
    }
}
