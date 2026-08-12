package org.finos.fluxnova.bpm.test.plugin.domain.script;

import java.util.List;

public record ScriptMetadata(
        Integer totalLines,
        List<ScriptMetrics> scriptCoverageMetadata
) { }
