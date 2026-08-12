package org.finos.fluxnova.bpm.test.plugin.impl.sonar;

import org.finos.fluxnova.bpm.test.TestException;
import org.finos.fluxnova.bpm.test.plugin.domain.script.LinesType;
import org.finos.fluxnova.bpm.test.plugin.domain.sonar.SonarScriptCoverageElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.finos.fluxnova.bpm.test.plugin.utils.Constants.*;
import static org.finos.fluxnova.bpm.test.plugin.utils.SonarXMLConstants.*;

public class SonarCoverageReporter {
    protected List<SonarScriptCoverageElement> sonarScriptCoverageElements;
    protected String buildPath;
    protected String srcPath;

    private static final Logger logger = LoggerFactory.getLogger("sonar-coverage-generation");

    public void init(Path buildPath, Path srcPath) {
        this.buildPath = buildPath.toString();
        this.srcPath = srcPath.toString();
        this.sonarScriptCoverageElements = new ArrayList<>();
    }

    public void addSonarCoverageElement(SonarScriptCoverageElement element) {
        this.sonarScriptCoverageElements.add(element);
    }

    public Document toSonarXML() {
        try {
            Document sonarCoverageXML = transformCoverageMetrics();
            writeSonarCoverageFile(sonarCoverageXML);
            return sonarCoverageXML;
        } catch (Exception e) {
            logger.error("Error generating sonar coverage report: {}", e.getMessage(), e);
            return null;
        }
    }

    private Document transformCoverageMetrics() throws ParserConfigurationException {
        Document sonarReportXML = initSonarXML();
        Element coverageElement = (Element) sonarReportXML.getElementsByTagName(COVERAGE_TAG_NAME).item(0);
        for (SonarScriptCoverageElement scriptElement : sonarScriptCoverageElements) {
            try {
                appendScriptCoverageMetrics(sonarReportXML, scriptElement, coverageElement);
            } catch (Exception e) {
                String identifier = isInline(scriptElement.activityId()) ?
                        scriptElement.processDefinitionKey() + ":" + scriptElement.activityId() :
                        scriptElement.filePath();
                logger.error("Error generating sonar metrics for {}", identifier, e);
            }
        }
        return sonarReportXML;
    }

    private void appendScriptCoverageMetrics(Document sonarReport, SonarScriptCoverageElement script, Element coverageElement) {
        Path filePath = getFilePath(script, isInline(script.activityId()));
        Element fileElement = sonarReport.createElement(FILE_TAG_NAME);
        fileElement.setAttribute(PATH_ATTR, filePath.toString());
        coverageElement.appendChild(fileElement);
        appendLineMetrics(fileElement, LinesType.COVERED, script.coveredLines());
        appendLineMetrics(fileElement, LinesType.MISSED, script.missedLines());
    }

    private Document initSonarXML() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element coverageElement = doc.createElement(COVERAGE_TAG_NAME);
        coverageElement.setAttribute(VERSION_ATTR, VERSION);
        doc.appendChild(coverageElement);
        return doc;
    }

    private Path getFilePath(SonarScriptCoverageElement element, boolean isInline) {
        if (isInline) {
            String fileName = element.processDefinitionKey() + "_" + element.activityId() + "." + getFileExtension(element.scriptFormat());
            return Path.of(srcPath, SONAR_DIRECTORY, GENERATED_DIRECTORY, SCRIPTS_DIRECTORY, fileName);
        } else {
            String externalScriptFileSrcPath = getExternalScriptSourceFilePath(element.filePath());
            return Path.of(externalScriptFileSrcPath);
        }
    }

    private boolean isInline(String activityId) {
        return activityId != null;
    }

    private void appendLineMetrics(Element fileElement, LinesType linesType, Collection<String> lines) {
        for (String line : lines) {
            Element lineElement = fileElement.getOwnerDocument().createElement(LINE_TO_COVER_TAG_NAME);
            lineElement.setAttribute(LINE_NUMBER_ATTR, line);
            lineElement.setAttribute(COVERED_ATTR, linesType == LinesType.COVERED ? "true" : "false");
            fileElement.appendChild(lineElement);
        }
    }

    private void writeSonarCoverageFile(Document sonarCoverageXML) throws IOException, TransformerException {
        Path sonarReport = Path.of(buildPath, COVERAGE_COLLECTION_DIRECTORY, SONAR_DIRECTORY, "coverage.xml");
        Files.createDirectories(sonarReport.getParent());
        File output = sonarReport.toFile();
        Transformer transformerFactory = TransformerFactory.newInstance().newTransformer();
        transformerFactory.setOutputProperty(OutputKeys.INDENT, "yes");
        transformerFactory.transform(new DOMSource(sonarCoverageXML), new StreamResult(output));
    }

    private String getFileExtension(String scriptFormat) {
        return scriptFormat.equalsIgnoreCase("groovy") ? scriptFormat : "js";
    }

    private String getExternalScriptSourceFilePath(String filePath) {
        String[] filePaths = filePath.split(TEST_CLASSES);
        String sourceRelativePath = filePaths[filePaths.length - 1];
        String sourceAbsolutePath = getAbsoluteSourceFilePath(sourceRelativePath);
        if (sourceAbsolutePath != null) {
            return sourceAbsolutePath;
        } else {
            logger.error("External script file not found for path: {}", filePath);
            throw new TestException("External script file not found for path: " + filePath);
        }
    }

    private String getAbsoluteSourceFilePath(String sourceFileRelativePath) {
        try (Stream<Path> paths = Files.walk(Path.of(srcPath))) {
            return paths
                    .map(Path::toString)
                    .filter(string -> string.endsWith(sourceFileRelativePath) && !string.contains(buildPath))
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            logger.error("Error checking file location for {}: {}", sourceFileRelativePath, e.getMessage());
            return null;
        }
    }
}
