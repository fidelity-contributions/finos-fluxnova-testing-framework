package org.finos.fluxnova.bpm.test.scripting

class ScriptTestException extends Exception {
    ScriptTestException(String message) {
        super(message)
    }

    ScriptTestException(String message, Throwable e) {
        super(message, e)
    }
}
