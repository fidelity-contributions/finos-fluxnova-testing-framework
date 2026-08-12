package org.finos.fluxnova.bpm.test.plugin.domain.sonar;

import java.util.List;
import java.util.Set;

public record SonarScriptCoverageElement(String processDefinitionKey, String activityId, String scriptFormat, String filePath, Set<String> coveredLines, List<String> missedLines) {
    public SonarScriptCoverageElement(String filePath, Set<String> coveredLines, List<String> missedLines) {
        this(null, null, null, filePath, coveredLines, missedLines);
    }

    public SonarScriptCoverageElement(String processDefinitionKey, String activityId, String scriptFormat, Set<String> coveredLines, List<String> missedLines) {
        this(processDefinitionKey, activityId, scriptFormat, null, coveredLines, missedLines);
    }
}
