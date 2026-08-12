package org.finos.fluxnova.bpm.test.plugin;

import org.finos.fluxnova.bpm.test.coverage.CoverageProperties;
import org.finos.fluxnova.bpm.test.plugin.impl.CoverageCollector;
import org.apache.maven.execution.MavenSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CoverageCollectorPluginTest {

    private static final File buildDir = new File("test");
    private static final File srcDir = new File("src");

    @Mock
    CoverageCollector coverageCollector;

    @Mock
    MavenSession mavenSession;

    @InjectMocks
    CoverageCollectorPlugin coverageCollectorPlugin;

    @BeforeEach
    void setup() throws Exception {
        setMojoParameter("buildDirectory", buildDir);
        setMojoParameter("srcDirectory", srcDir);
    }

    @Test
    void testMojo_coverage() throws Exception {
        try (MockedStatic<CoverageProperties> coveragePropertiesMockedStatic = Mockito.mockStatic(CoverageProperties.class)) {
            coveragePropertiesMockedStatic.when(CoverageProperties::getSkipCoverage).thenReturn(false);
            assertThatCode(coverageCollectorPlugin::execute).doesNotThrowAnyException();
            verify(coverageCollector, times(1)).collect(buildDir.toPath(), srcDir.toPath(), mavenSession);
        }
    }

    @Test
    void testMojo_coverageSkipped() throws Exception {
        try (MockedStatic<CoverageProperties> coveragePropertiesMockedStatic = Mockito.mockStatic(CoverageProperties.class)) {
            coveragePropertiesMockedStatic.when(CoverageProperties::getSkipCoverage).thenReturn(true);
            assertThatCode(coverageCollectorPlugin::execute).doesNotThrowAnyException();
            verify(coverageCollector, times(0)).collect(buildDir.toPath(), srcDir.toPath(), mavenSession);
        }
    }

    private void setMojoParameter(String fieldName, Object value) throws Exception {
        Field field = coverageCollectorPlugin.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(coverageCollectorPlugin, value);
    }
}

