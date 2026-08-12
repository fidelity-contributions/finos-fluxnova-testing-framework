package org.finos.fluxnova.bpm.test.plugin.utils;

import org.finos.fluxnova.bpm.test.plugin.domain.ModelTestMetadata;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.finos.fluxnova.bpm.test.plugin.utils.Utils.deleteGeneratedInlineFiles;
import static org.finos.fluxnova.bpm.test.plugin.utils.Utils.getGeneratedFilePath;

public class TestHelpers {

    public static void deleteTestGeneratedFiles() {
        deleteGeneratedInlineFiles(Path.of("src"));
    }

    public static void deleteTestCoverageFiles() {
        Path codeCoverageFilesPath = Path.of("target", "coverage-collection");
        FileSystemUtils.deleteRecursively(codeCoverageFilesPath.toFile());
    }

    public static void createFile(Path path, String content) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, content);
    }

    public static String getGeneratedFileContents(String fileName) throws IOException {
        Path scriptPath = getGeneratedFilePath(Path.of("src")).resolve(fileName);
        return Files.readString(scriptPath);
    }

    public static ModelTestMetadata modelUnderTest(String fileName, List<ModelTestMetadata> modelTestMetadataList) {
        Optional<ModelTestMetadata> modelTestMetadataOptional = modelTestMetadataList.stream().filter(modelTestMetadata -> modelTestMetadata.getFileName().equals(fileName)).findFirst();
        return modelTestMetadataOptional.orElse(null);
    }
}
