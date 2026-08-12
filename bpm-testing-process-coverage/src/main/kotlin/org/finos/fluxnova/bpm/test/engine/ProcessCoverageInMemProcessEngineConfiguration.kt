package org.finos.fluxnova.bpm.test.engine

import org.finos.fluxnova.bpm.test.engine.ProcessCoverageConfigurator.initializeProcessCoverageExtensions
import org.finos.fluxnova.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration

/**
 * Standalone in memory process engine configuration additionally configuring
 * flow node, sequence flow and compensation listeners for process coverage
 * testing.
 *
 * @author z0rbas
 */
class ProcessCoverageInMemProcessEngineConfiguration : StandaloneInMemProcessEngineConfiguration() {
    override fun init() {
        initializeProcessCoverageExtensions(this)
        super.init()
    }
}
