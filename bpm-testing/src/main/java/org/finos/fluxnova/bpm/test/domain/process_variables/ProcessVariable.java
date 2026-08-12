package org.finos.fluxnova.bpm.test.domain.process_variables;

public record ProcessVariable(
        ProcessVariableType type,
        String variableName,
        Object variableValue
) { }
