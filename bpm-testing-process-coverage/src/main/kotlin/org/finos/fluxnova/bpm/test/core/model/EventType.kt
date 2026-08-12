package org.finos.fluxnova.bpm.test.core.model

/**
 * Type of an event as happend during Fluxnova execution.
 *
 * @author dominikhorn
 */
enum class EventType {
    /**
     * Flow node start.
     */
    START,

    /**
     * Flow node end.
     */
    END,

    /**
     * Transition take (only for sequence flow).
     */
    TAKE
}
