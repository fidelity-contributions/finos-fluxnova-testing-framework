package org.finos.fluxnova.bpm.test.coverage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProcessCoverageTest {

    @BeforeEach
    void setUp() {
        deleteTestFiles();
    }

    @Test
    void register_metadataFileCreated() throws Exception {
        try (MockedStatic<CoverageProperties> coveragePropertiesMockedStatic = Mockito.mockStatic(CoverageProperties.class)) {
            coveragePropertiesMockedStatic.when(CoverageProperties::getSkipCoverage).thenReturn(false);
            ProcessCoverage.register("some_process", ProcessCoverageTest.class);
            String actualContent = getFileContent();
            String expectedContent = """
                    {"processDefinitionKey":"some_process","testClass":"org.finos.fluxnova.bpm.test.coverage.ProcessCoverageTest"}""";
            assertEquals(actualContent, expectedContent);
        }
    }

    @Test
    void register_errorLoggedWhenProcessDefinitionKeyMissing() {
        try (MockedStatic<LoggerFactory> loggerFactoryMock = Mockito.mockStatic(LoggerFactory.class);
             MockedStatic<CoverageProperties> coveragePropertiesMockedStatic = Mockito.mockStatic(CoverageProperties.class)){
             coveragePropertiesMockedStatic.when(CoverageProperties::getSkipCoverage).thenReturn(false);
             Logger logger = Mockito.mock(Logger.class);
             loggerFactoryMock.when(() -> LoggerFactory.getLogger(eq(ProcessCoverage.class))).thenReturn(logger);
             assertDoesNotThrow(() -> ProcessCoverage.register(null, ProcessCoverageTest.class));
        }
    }

    @Test
    void register_metadataNotFileCreatedWhenCoverageSkipped() throws Exception {
        try (MockedStatic<CoverageProperties> coveragePropertiesMockedStatic = Mockito.mockStatic(CoverageProperties.class)) {
            coveragePropertiesMockedStatic.when(CoverageProperties::getSkipCoverage).thenReturn(true);
            ProcessCoverage.register("some_process", ProcessCoverageTest.class);
            String actualContent = getFileContent();
            assertNull(actualContent);
        }
    }

    private static String getFileContent() throws IOException {
        Path filePath = Path.of("target/coverage-collection");
        FilenameFilter filenameFilter = (dir, path) -> path.endsWith("metadata.json");
        File[] files = filePath.toFile().listFiles(filenameFilter);
        if (files != null) {
            for (File file : files) {
                String content = Files.readString(file.toPath());
                if (content.contains("some_process")) {
                    return content;
                }
            }
        }
        return null;
    }

    private static void deleteTestFiles() {
        Path codeCoverageFilesPath = Path.of("target", "coverage-collection");
        FileSystemUtils.deleteRecursively(codeCoverageFilesPath.toFile());
    }
}
