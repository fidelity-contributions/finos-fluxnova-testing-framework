package org.finos.fluxnova.bpm.test.plugin.domain.suite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Run(
        String id,
        List<Event> events
) { }
