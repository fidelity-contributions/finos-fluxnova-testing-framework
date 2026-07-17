package org.finos.fluxnova.bpm.test.engine

import org.finos.fluxnova.bpm.test.engine.ProcessCoverageConfigurator.initializeProcessCoverageExtensions
import org.finos.fluxnova.bpm.engine.spring.SpringProcessEngineConfiguration

/**
 * Spring process engine configuration additionally configuring
 * flow node, sequence flow and compensation listeners for process coverage
 * testing.
 *
 *
 * Created by lldata on 20-10-2016.
 */
class SpringProcessWithCoverageEngineConfiguration : SpringProcessEngineConfiguration() {
    override fun init() {
        initializeProcessCoverageExtensions(this)
        super.init()
    }
}
