package org.finos.fluxnova.bpm.test.plugin.domain.script;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ScriptRunInstance(
        String scriptName,
        String activityId,
        String processDefinitionKey,
        Integer totalLines,
        List<String> coveredLines,
        boolean isExternal
) {}
