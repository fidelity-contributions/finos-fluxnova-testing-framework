package org.finos.fluxnova.bpm.test.core.export

import org.finos.fluxnova.bpm.test.core.model.Coverage
import org.finos.fluxnova.bpm.test.core.model.Model
import org.finos.fluxnova.bpm.test.core.model.Suite


data class CoverageStateResult(
    val suites: Collection<Suite>,
    val models: Collection<Model>
) : Coverage {
    override fun getEvents() = suites.map { it.getEvents() }.flatten()

    override fun getEvents(modelKey: String) = suites.map { it.getEvents(modelKey) }.flatten()

}
