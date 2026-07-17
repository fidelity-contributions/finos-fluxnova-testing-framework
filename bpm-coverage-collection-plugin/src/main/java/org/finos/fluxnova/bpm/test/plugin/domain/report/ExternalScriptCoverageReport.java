package org.finos.fluxnova.bpm.test.plugin.domain.report;

public record ExternalScriptCoverageReport(
        CoverageMetadata metadata,
        double coverage,
        boolean pass
) { }
