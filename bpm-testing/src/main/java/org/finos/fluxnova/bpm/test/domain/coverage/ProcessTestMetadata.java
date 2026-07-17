package org.finos.fluxnova.bpm.test.domain.coverage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProcessTestMetadata(
        String processDefinitionKey,
        String testClass
) { }
