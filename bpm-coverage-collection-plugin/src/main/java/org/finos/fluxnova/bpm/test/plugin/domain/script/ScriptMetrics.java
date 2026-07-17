package org.finos.fluxnova.bpm.test.plugin.domain.script;

import java.util.List;

public record ScriptMetrics(
        String activityId,
        String scriptFormat,
        List<String> lines,
        String error
) { }
