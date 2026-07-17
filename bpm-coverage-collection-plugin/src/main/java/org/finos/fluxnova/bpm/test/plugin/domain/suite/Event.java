package org.finos.fluxnova.bpm.test.plugin.domain.suite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Event(
        String type,
        String modelKey,
        String definitionKey
) { }
