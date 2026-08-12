package org.finos.fluxnova.bpm.test.plugin.impl.sonar;

import org.finos.fluxnova.bpm.test.TestException;
import org.finos.fluxnova.bpm.test.plugin.domain.sonar.SonarScriptCoverageElement;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SonarCoverageReporterTest {

    private static MockedStatic<Files> filesMock;
    private static MockedStatic<LoggerFactory> loggerFactoryMock;
    private static Logger logger;

    private static final String BUILD_PATH = "target";
    private static final String SRC_PATH = "src" + File.separator + "main" + File.separator + "java";

    @InjectMocks
    SonarCoverageReporter sonarCoverageReporter;

    @BeforeAll
    static void init() {
        logger = mock(Logger.class);
        loggerFactoryMock = mockStatic(LoggerFactory.class);
        loggerFactoryMock.when(() -> LoggerFactory.getLogger(anyString())).thenReturn(logger);
        filesMock = Mockito.mockStatic(Files.class, CALLS_REAL_METHODS);
    }

    @AfterAll
    static void close() {
        loggerFactoryMock.close();
        filesMock.close();
    }

    @BeforeEach
    void before() {
        Path buildPath = Path.of(BUILD_PATH);
        Path srcPath = Path.of(SRC_PATH);
        sonarCoverageReporter.init(buildPath, srcPath);
        sonarCoverageReporter.sonarScriptCoverageElements = new ArrayList<>();
    }

    @AfterEach
    void after() {
        filesMock.reset();
    }

    @Test
    void init_initializesSuccessfully() {
        assertEquals(BUILD_PATH, sonarCoverageReporter.buildPath);
        assertEquals(SRC_PATH, sonarCoverageReporter.srcPath);
        assertEquals(List.of(), sonarCoverageReporter.sonarScriptCoverageElements);
    }

    @Test
    void addSonarCoverageElement_addsScriptElementsSuccessfully() {
        SonarScriptCoverageElement inlineElement =
                new SonarScriptCoverageElement("ProcessDefKey_1", "Activity_1234", "groovy", Set.of("1", "2", "3"), List.of("1", "2", "3", "4"));
        sonarCoverageReporter.addSonarCoverageElement(inlineElement);
        SonarScriptCoverageElement externalElement =
                new SonarScriptCoverageElement("/path/to/file.groovy", Set.of("1", "2"), List.of("1", "2", "3"));
        sonarCoverageReporter.addSonarCoverageElement(externalElement);

        assertEquals(2, sonarCoverageReporter.sonarScriptCoverageElements.size());

        SonarScriptCoverageElement inlineSonarScriptCoverageElement = sonarCoverageReporter.sonarScriptCoverageElements.getFirst();
        SonarScriptCoverageElement externalSonarScriptCoverageElement = sonarCoverageReporter.sonarScriptCoverageElements.get(1);

        assertNotNull(inlineSonarScriptCoverageElement);
        assertEquals("ProcessDefKey_1", inlineSonarScriptCoverageElement.processDefinitionKey());
        assertEquals("Activity_1234", inlineSonarScriptCoverageElement.activityId());
        assertEquals("groovy", inlineSonarScriptCoverageElement.scriptFormat());
        assertEquals(Set.of("1", "2", "3"), inlineSonarScriptCoverageElement.coveredLines());
        assertEquals(List.of("1", "2", "3", "4"), inlineSonarScriptCoverageElement.missedLines());
        assertNull(inlineSonarScriptCoverageElement.filePath());

        assertNotNull(externalSonarScriptCoverageElement);
        assertEquals("/path/to/file.groovy", externalSonarScriptCoverageElement.filePath());
        assertEquals(Set.of("1", "2"), externalSonarScriptCoverageElement.coveredLines());
        assertEquals(List.of("1", "2", "3"), externalSonarScriptCoverageElement.missedLines());
        assertNull(externalSonarScriptCoverageElement.activityId());
        assertNull(externalSonarScriptCoverageElement.processDefinitionKey());
        assertNull(externalSonarScriptCoverageElement.scriptFormat());
    }

    @Test
    void toSonarXML_handlesMultipleElements_mixedInlineAndExternal() {
        SonarScriptCoverageElement inlineElementGroovy =
                new SonarScriptCoverageElement("GroovyProcessDefKey_1", "Activity_1234", "groovy", Set.of("1", "2", "3"), List.of("1", "2", "3", "4"));
        SonarScriptCoverageElement inlineElementJs =
                new SonarScriptCoverageElement("JsProcessDefKey_1", "Activity_1235", "js", Set.of("1", "2"), List.of("1", "2", "3", "4", "5"));
        SonarScriptCoverageElement externalElement =
                new SonarScriptCoverageElement(File.separator + "target" + File.separator + "test-classes" + File.separator + "external_script.js", Set.of("1", "2"), List.of("1", "2", "3"));

        Path foundFile = Path.of(SRC_PATH, "external_script.js");
        filesMock.when(() -> Files.walk(any(Path.class)))
                .thenReturn(Stream.of(foundFile));

        sonarCoverageReporter.addSonarCoverageElement(inlineElementGroovy);
        sonarCoverageReporter.addSonarCoverageElement(inlineElementJs);
        sonarCoverageReporter.addSonarCoverageElement(externalElement);

        Document doc = sonarCoverageReporter.toSonarXML();
        assertNotNull(doc);
        NodeList fileElements = doc.getElementsByTagName("file");
        assertEquals(3, fileElements.getLength());
        assertFileElement
                (fileElements, 0, "src/main/java/sonar/generated/scripts/GroovyProcessDefKey_1_Activity_1234.groovy", 3, 4);
        assertFileElement
                (fileElements, 1, "src/main/java/sonar/generated/scripts/JsProcessDefKey_1_Activity_1235.js", 2, 5);
        assertFileElement
                (fileElements, 2, "src/main/java/external_script.js", 2, 3);

    }

    @Test
    void toSonarXML_firstExternalFileSomeIOError_gracefullyHandles() {
        SonarScriptCoverageElement failedExternalElement =
                new SonarScriptCoverageElement( File.separator + "target" + File.separator + "test-classes" + File.separator + "failed_external_script.js", Set.of("1", "2"), List.of("1", "2", "3"));
        SonarScriptCoverageElement externalElement =
                new SonarScriptCoverageElement( File.separator + "target" + File.separator + "test-classes" + File.separator + "external_script.groovy", Set.of("1", "2"), List.of("1", "2", "3"));

        Path foundFile = Path.of(SRC_PATH, "external_script.groovy");
        filesMock.when(() -> Files.walk(any(Path.class)))
                .thenThrow(new IOException("IO Error"))
                .thenReturn(Stream.of(foundFile));

        sonarCoverageReporter.addSonarCoverageElement(failedExternalElement);
        sonarCoverageReporter.addSonarCoverageElement(externalElement);

        Document doc = sonarCoverageReporter.toSonarXML();

        assertNotNull(doc);
        NodeList fileElements = doc.getElementsByTagName("file");
        assertEquals(1, fileElements.getLength());
        assertFileElement
                (fileElements, 0, "src/main/java/external_script.groovy", 2, 3);

        verify(logger, times(1)).error("External script file not found for path: {}",  File.separator + "target" + File.separator + "test-classes" + File.separator + "failed_external_script.js");
        verify(logger, times(1)).error(eq("Error generating sonar metrics for {}"), eq( File.separator + "target" + File.separator + "test-classes" + File.separator + "failed_external_script.js"), any(TestException.class));
    }

    @Test
    void toSonarXML_firstExternalFileNotFound_notIncludedInReport() {
        SonarScriptCoverageElement missingExternalScript =
                new SonarScriptCoverageElement( File.separator + "target" + File.separator + "test-classes" + File.separator + "missing_external_script.js", Set.of("1", "2"), List.of("1", "2", "3"));
        SonarScriptCoverageElement externalElement =
                new SonarScriptCoverageElement( File.separator + "target" + File.separator + "test-classes" + File.separator + "external_script.groovy", Set.of("1", "2"), List.of("1", "2", "3"));

        Path foundFile = Path.of(SRC_PATH, "external_script.groovy");
        Path buildFile = Path.of(BUILD_PATH, "external_script.groovy");
        filesMock.when(() -> Files.walk(any(Path.class)))
                .thenReturn(Stream.of(buildFile))
                .thenReturn(Stream.of(foundFile));

        sonarCoverageReporter.addSonarCoverageElement(missingExternalScript);
        sonarCoverageReporter.addSonarCoverageElement(externalElement);

        Document doc = sonarCoverageReporter.toSonarXML();

        assertNotNull(doc);
        NodeList fileElements = doc.getElementsByTagName("file");
        assertEquals(1, fileElements.getLength());
        assertFileElement
                (fileElements, 0, "src/main/java/external_script.groovy", 2, 3);

        verify(logger, times(1)).error("External script file not found for path: {}",  File.separator + "target" + File.separator + "test-classes" + File.separator + "missing_external_script.js");
        verify(logger, times(1)).error(eq("Error generating sonar metrics for {}"), eq( File.separator + "target" + File.separator + "test-classes" + File.separator + "missing_external_script.js"), any(TestException.class));
    }

    @Test
    void toSonarXML_generalErrorOnCoverageFileGeneration_fails() {
        SonarScriptCoverageElement externalElement =
                new SonarScriptCoverageElement("/target/test-classes/external_script.groovy", Set.of("1", "2"), List.of("1", "2", "3"));

        Path foundFile = Path.of(SRC_PATH, "external_script.groovy");
        filesMock.when(() -> Files.walk(any(Path.class)))
                .thenThrow(new IOException("IO Error"))
                .thenReturn(Stream.of(foundFile));

        sonarCoverageReporter.addSonarCoverageElement(externalElement);

        filesMock.when(() -> Files.createDirectories(any(Path.class)))
                .thenThrow(new IOException("IO error"));

        Document doc = sonarCoverageReporter.toSonarXML();

        assertNull(doc);
        verify(logger, times(1)).error(eq("Error generating sonar coverage report: {}"), eq("IO error"), any(IOException.class));
    }

    private void assertFileElement(NodeList fileElements, int index, String expectedPath, int expectedCoveredLines, int expectedMissedLines) {
        var fileElement = fileElements.item(index);
        assertEquals(Paths.get(expectedPath).normalize(), Paths.get(fileElement.getAttributes().getNamedItem("path").getNodeValue()).normalize());

        int coveredLinesCount = 0;
        int missedLinesCount = 0;

        NodeList lineElements = fileElement.getChildNodes();
        for (int i = 0; i < lineElements.getLength(); i++) {
            var lineElement = lineElements.item(i);
            if ("lineToCover".equals(lineElement.getNodeName())) {
                boolean isCovered = "true".equals(lineElement.getAttributes().getNamedItem("covered").getNodeValue());
                if (isCovered) {
                    coveredLinesCount++;
                } else {
                    missedLinesCount++;
                }
            }
        }

        assertEquals(expectedCoveredLines, coveredLinesCount);
        assertEquals(expectedMissedLines, missedLinesCount);
    }
}
