package org.finos.fluxnova.bpm.test.plugin.utils;

import org.finos.fluxnova.bpm.model.bpmn.instance.Script;
import org.finos.fluxnova.bpm.model.bpmn.instance.ScriptTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.finos.fluxnova.bpm.test.plugin.utils.Constants.*;

public class Utils {

    private static final Logger logger = LoggerFactory.getLogger("coverage-utils");

    private Utils() {}

    public static double calculateCoverage(double totalCount, double totalExecuted) {
        double coverage =  (totalExecuted / totalCount) * 100;
        return roundTo2Decimal(coverage);
    }

    public static double roundTo2Decimal(double original) {
        return Math.round(original * 100) / 100.0;
    }

    public static List<File> collectFiles(File directory, String ... fileTypes) {
        List<File> bpmnFiles = new ArrayList<>();
        searchRecursively(directory, bpmnFiles, fileTypes);
        return bpmnFiles;
    }

    public static boolean isScriptValid(ScriptTask scriptTask) {
        Script script = scriptTask.getScript();
        String scriptFormat = (scriptTask.getScriptFormat()!= null) ? scriptTask.getScriptFormat().toLowerCase() : null;
        return script != null && !script.getTextContent().isBlank() && isScriptFormatValid(scriptFormat);
    }
    public static boolean isScriptFormatValid(String scriptFormat){
        List<String> validScripts = Arrays.asList("groovy", "js", "javascript");
        return validScripts.stream().anyMatch(scriptFormat::equals);
    }

    public static boolean coverageThresholdMet(double coverage, double threshold) {
        return coverage >= threshold;
    }

    public static void logCoverageWarning(String name, TestResourceType type, double threshold) {
        logger.warn("Coverage below {}\t|\t{} ({})", threshold, name, type);
    }

    public static Path getGeneratedFilePath(Path srcDir) {
        return srcDir.resolve(SONAR_DIRECTORY).resolve(GENERATED_DIRECTORY).resolve(SCRIPTS_DIRECTORY);
    }

    public static void deleteGeneratedInlineFiles(Path srcDir) {
        Path generatedFilesPath = getGeneratedFilePath(srcDir);
        FileSystemUtils.deleteRecursively(generatedFilesPath.toFile());
    }

    private static void searchRecursively(File file, List<File> filesToCollect, String... fileTypes) {
        if (file.isDirectory() && !file.getPath().contains("node_modules")) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                searchRecursively(f, filesToCollect, fileTypes);
            }
        } else if (isFileType(file.getName(), fileTypes)) {
            filesToCollect.add(file);
        }
    }

    private static boolean isFileType(String fileName, String... fileTypes) {
        return Arrays.stream(fileTypes).anyMatch(fileName::endsWith);
    }
}
