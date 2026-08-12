package org.finos.fluxnova.bpm.test.scripting.mocks

interface ConnectorExtension {
    void setVariable(String key, Object value)
    Object getVariable(String key)
    boolean hasVariable(String key)
}