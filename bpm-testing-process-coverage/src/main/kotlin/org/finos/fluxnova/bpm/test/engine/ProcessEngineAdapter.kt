package org.finos.fluxnova.bpm.test.engine

import io.github.oshai.kotlinlogging.KotlinLogging
import org.finos.fluxnova.bpm.engine.ProcessEngine
import org.finos.fluxnova.bpm.engine.impl.bpmn.parser.BpmnParseListener
import org.finos.fluxnova.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.finos.fluxnova.bpm.test.core.model.Collector

private val logger = KotlinLogging.logger {}

class ProcessEngineAdapter(
    private val processEngine: ProcessEngine,
    private val coverageCollector: Collector
) {

    /**
     * Sets the test run state for the coverage listeners. logging.
     * {@see ProcessCoverageInMemProcessEngineConfiguration}
     */
    fun initializeListeners() {
        val processEngineConfiguration = processEngine.processEngineConfiguration as ProcessEngineConfigurationImpl

        val bpmnParseListeners = processEngineConfiguration.customPostBPMNParseListeners

        for (parseListener: BpmnParseListener? in bpmnParseListeners) {
            if (parseListener is ElementCoverageParseListener) {
                parseListener.setCoverageState(this.coverageCollector)
            }
        }

        // Compensation event handler

        // Compensation event handler
        val compensationEventHandler = processEngineConfiguration.getEventHandler("compensate")
        if (compensationEventHandler is CompensationEventCoverageHandler) {
            compensationEventHandler.setCoverageState(this.coverageCollector)
        } else {
            logger.warn(
                "CompensationEventCoverageHandler not registered with process engine configuration!"
                        + " Compensation boundary events coverage will not be registered."
            )
        }
    }

}
