package org.finos.fluxnova.bpm.test.data.adapter

/**
 * Exception indicating that a variable is not found.
 * @param reason reason.
 */
class VariableNotFoundException(reason: String?) : RuntimeException(reason)
