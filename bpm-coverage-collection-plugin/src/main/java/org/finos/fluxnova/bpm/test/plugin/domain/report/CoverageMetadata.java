package org.finos.fluxnova.bpm.test.plugin.domain.report;

import com.fasterxml.jackson.annotation.JsonInclude;

import static org.finos.fluxnova.bpm.test.coverage.CoverageProperties.getThreshold;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CoverageMetadata(String name, String processDefinitionKey, double threshold) {
    public static CoverageMetadata setMetadataForModelCoverage(String bpmn, String processDefinitionKey) {
        return new CoverageMetadata(bpmn, processDefinitionKey, getThreshold());
    }

    public static CoverageMetadata setMetadataForExtScriptCoverage(String scriptName) {
        return new CoverageMetadata(scriptName, null, getThreshold());
    }
}
