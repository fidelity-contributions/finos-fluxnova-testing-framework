package org.finos.fluxnova.bpm.test.plugin.domain.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.finos.fluxnova.bpm.test.plugin.domain.script.ScriptCoverage;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ModelCoverageReport(
        CoverageMetadata metadata,
        double processCoverage,
        double scriptCoverage,
        List<ScriptCoverage> scripts,
        boolean pass,
        String error
) {
    public static ModelCoverageReport setError(String bpmn, String error) {
        CoverageMetadata coverageMetadata = CoverageMetadata.setMetadataForModelCoverage(bpmn, null);
        return new ModelCoverageReport(coverageMetadata, 0.0, 0.0, null, false, error);
    }
}
