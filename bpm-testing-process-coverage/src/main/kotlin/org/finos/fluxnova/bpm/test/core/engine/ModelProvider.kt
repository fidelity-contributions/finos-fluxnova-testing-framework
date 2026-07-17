package org.finos.fluxnova.bpm.test.core.engine

import org.finos.fluxnova.bpm.test.core.model.Model

/**
 * Provider for process model.
 */
interface ModelProvider {
    /**
     * Retrieves a model by key.
     * @param key process definition key
     * @return model.
     */
    fun getModel(key: String): Model
}
