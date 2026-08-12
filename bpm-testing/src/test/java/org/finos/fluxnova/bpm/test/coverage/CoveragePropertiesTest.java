package org.finos.fluxnova.bpm.test.coverage;

import org.finos.fluxnova.bpm.test.util.Constants;
import org.finos.fluxnova.bpm.test.util.SystemWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.finos.fluxnova.bpm.test.coverage.CoverageProperties.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CoveragePropertiesTest {

    @Test
    void getThreshold_returnSetThreshold() {
        try (MockedStatic<SystemWrapper> systemWrapperMockedStatic = Mockito.mockStatic(SystemWrapper.class)){
            systemWrapperMockedStatic.when(() -> SystemWrapper.getProperty("coverageThreshold")).thenReturn("20");
            assertEquals(20.0, getThreshold());
        }
    }

    @Test
    void getThreshold_returnSetThresholdMorePrecision() {
        try (MockedStatic<SystemWrapper> systemWrapperMockedStatic = Mockito.mockStatic(SystemWrapper.class)){
            systemWrapperMockedStatic.when(() -> SystemWrapper.getProperty("coverageThreshold")).thenReturn("20.5");
            assertEquals(20.5, getThreshold());
        }
    }

    @Test
    void getThreshold_returnDefaultWhenNoSetThreshold() {
        try (MockedStatic<SystemWrapper> systemWrapperMockedStatic = Mockito.mockStatic(SystemWrapper.class)){
            systemWrapperMockedStatic.when(() -> SystemWrapper.getProperty("coverageThreshold")).thenReturn(null);
            assertEquals(80.0, getThreshold());
        }
    }

    @Test
    void getThresholdForProcessCoverageConfiguration_returnSetThresholdAsDecimal() {
        try (MockedStatic<SystemWrapper> systemWrapperMockedStatic = Mockito.mockStatic(SystemWrapper.class)){
            systemWrapperMockedStatic.when(() -> SystemWrapper.getProperty("coverageThreshold")).thenReturn("20");
            assertEquals(0.2, getThresholdForProcessCoverageConfiguration());
        }
    }

    @Test
    void getThresholdForProcessCoverageConfiguration_returnDefaultWhenNoSetThresholdAsDecimal() {
        try (MockedStatic<SystemWrapper> systemWrapperMockedStatic = Mockito.mockStatic(SystemWrapper.class)){
            systemWrapperMockedStatic.when(() -> SystemWrapper.getProperty("coverageThreshold")).thenReturn(null);
            assertEquals(0.8, getThresholdForProcessCoverageConfiguration());
        }
    }

    @Test
    void getIgnoreCoverageFailure_returnDefaultAsFalseWhenNotSet() {
        try (MockedStatic<SystemWrapper> systemWrapperMockedStatic = Mockito.mockStatic(SystemWrapper.class)){
            systemWrapperMockedStatic.when(() -> SystemWrapper.getProperty(Constants.IGNORE_COVERAGE_FAILURE_PROPERTY)).thenReturn(null);
            assertFalse(getIgnoreCoverageFailure());
        }
    }

    @Test
    void getIgnoreCoverageFailure_returnFalseWhenSetAsFalse() {
        try (MockedStatic<SystemWrapper> systemWrapperMockedStatic = Mockito.mockStatic(SystemWrapper.class)){
            systemWrapperMockedStatic.when(() -> SystemWrapper.getProperty(Constants.IGNORE_COVERAGE_FAILURE_PROPERTY)).thenReturn("false");
            assertFalse(getIgnoreCoverageFailure());
        }
    }

    @Test
    void getIgnoreCoverageFailure_returnTrueWhenSetAsTrue() {
        try (MockedStatic<SystemWrapper> systemWrapperMockedStatic = Mockito.mockStatic(SystemWrapper.class)){
            systemWrapperMockedStatic.when(() -> SystemWrapper.getProperty(Constants.IGNORE_COVERAGE_FAILURE_PROPERTY)).thenReturn("true");
            assertTrue(getIgnoreCoverageFailure());
        }
    }

    @Test
    void getSkipCoverage_returnDefaultAsFalseWhenNotSet() {
        try (MockedStatic<SystemWrapper> systemWrapperMockedStatic = Mockito.mockStatic(SystemWrapper.class)){
            systemWrapperMockedStatic.when(() -> SystemWrapper.getProperty(Constants.SKIP_COVERAGE_PROPERTY)).thenReturn(null);
            systemWrapperMockedStatic.when(() -> SystemWrapper.getProperty(Constants.SKIP_TESTS)).thenReturn(null);
            assertFalse(getSkipCoverage());
        }
    }

    @Test
    void getSkipCoverage_returnTrueWhenSkipCoverageSetAsTrue() {
        try (MockedStatic<SystemWrapper> systemWrapperMockedStatic = Mockito.mockStatic(SystemWrapper.class)){
            systemWrapperMockedStatic.when(() -> SystemWrapper.getProperty(Constants.SKIP_COVERAGE_PROPERTY)).thenReturn("true");
            systemWrapperMockedStatic.when(() -> SystemWrapper.getProperty(Constants.SKIP_TESTS)).thenReturn("false");
            assertTrue(getSkipCoverage());
        }
    }

    @Test
    void getSkipCoverage_returnTrueWhenSkipTestSetAsTrue() {
        try (MockedStatic<SystemWrapper> systemWrapperMockedStatic = Mockito.mockStatic(SystemWrapper.class)){
            systemWrapperMockedStatic.when(() -> SystemWrapper.getProperty(Constants.SKIP_TESTS)).thenReturn("true");
            systemWrapperMockedStatic.when(() -> SystemWrapper.getProperty(Constants.SKIP_COVERAGE_PROPERTY)).thenReturn("false");

            assertTrue(getSkipCoverage());
        }
    }

    @Test
    void getSkipCoverage_returnTrueWhenBothSetAsTrue() {
        try (MockedStatic<SystemWrapper> systemWrapperMockedStatic = Mockito.mockStatic(SystemWrapper.class)){
            systemWrapperMockedStatic.when(() -> SystemWrapper.getProperty(Constants.SKIP_TESTS)).thenReturn("true");
            systemWrapperMockedStatic.when(() -> SystemWrapper.getProperty(Constants.SKIP_COVERAGE_PROPERTY)).thenReturn("true");
            assertTrue(getSkipCoverage());
        }
    }
}
