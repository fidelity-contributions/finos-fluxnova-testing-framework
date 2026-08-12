package org.finos.fluxnova.bpm.test;

public class TestException extends RuntimeException {
    public TestException(String message, Throwable t) {
       super(message, t);
    }

    public TestException(String message) {
        super(message);
    }
}
