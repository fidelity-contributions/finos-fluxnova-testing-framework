package org.finos.fluxnova.bpm.test.plugin.utils;

public enum FileType {

    BPMN("bpmn"), JS("js"), GROOVY("groovy");

    private final String value;

    FileType(String value) {
        this.value = value;
    }

    public String getType() {
        return this.value;
    }
}
