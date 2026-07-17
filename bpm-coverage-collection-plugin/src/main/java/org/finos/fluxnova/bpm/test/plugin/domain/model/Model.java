package org.finos.fluxnova.bpm.test.plugin.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Model(
        String key,
        Integer totalElementCount
) { }
