package org.finos.fluxnova.bpm.test.plugin.domain.script;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = "missedLines")
public record ScriptCoverage(
        String activityId,
        double coverage,
        List<String> missedLines,
        String error
) {
    public static ScriptCoverage setError(String activityId, String error) {
        return new ScriptCoverage(activityId, 0.0, null, error);
    }
}
