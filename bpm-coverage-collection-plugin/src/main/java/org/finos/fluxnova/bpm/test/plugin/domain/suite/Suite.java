package org.finos.fluxnova.bpm.test.plugin.domain.suite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Suite(
        String id,
        List<Run> runs
) { }
