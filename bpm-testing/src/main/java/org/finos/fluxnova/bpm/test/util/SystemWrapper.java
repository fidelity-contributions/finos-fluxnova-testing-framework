package org.finos.fluxnova.bpm.test.util;

public class SystemWrapper {

    private SystemWrapper() {}

    public static String getProperty(String propertyName) {
        return System.getProperty(propertyName);
    }
}
