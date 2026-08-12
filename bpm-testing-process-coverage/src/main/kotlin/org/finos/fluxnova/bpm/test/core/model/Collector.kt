package org.finos.fluxnova.bpm.test.core.model

/**
 * Interface for collection coverage data.
 *
 * @author dominikhorn
 */
interface Collector {
    /**
     * Adds a new event.
     * @param event event to add.
     */
    fun addEvent(event: Event)
}
