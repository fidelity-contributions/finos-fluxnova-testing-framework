package org.finos.fluxnova.bpm.test.spring.platform

import org.finos.fluxnova.bpm.test.core.model.DefaultCollector
import org.finos.fluxnova.bpm.test.engine.ExecutionContextModelProvider
import org.finos.fluxnova.bpm.test.engine.ProcessEngineAdapter
import org.finos.fluxnova.bpm.test.spring.test.common.BaseProcessEngineCoverageTestExecutionListener
import org.finos.fluxnova.bpm.engine.ProcessEngine
import org.springframework.test.context.TestContext

/**
 * Test execution listener for process test coverage.
 * Can be used with spring testing framework to get process test coverage in spring tests.
 *
 * @author Jan Rohwer
 */
class ProcessEngineCoverageTestExecutionListener : BaseProcessEngineCoverageTestExecutionListener() {

    /**
     * The state of the current run (class and current method).
     */
    private val coverageCollector = DefaultCollector(ExecutionContextModelProvider())

    override fun getCoverageCollector() = coverageCollector

    override fun isTestClassExcluded(testContext: TestContext): Boolean {
        return super.isTestClassExcluded(testContext)
                || testContext.applicationContext.getBeanNamesForType(ProcessEngine::class.java).isEmpty()
    }

    override fun beforeTestClass(testContext: TestContext) {
        super.beforeTestClass(testContext)
        if (!isTestClassExcluded(testContext)) {
            ProcessEngineAdapter(getProcessEngine(testContext), coverageCollector).initializeListeners()
        }
    }

    private fun getProcessEngine(testContext: TestContext) = testContext.applicationContext.getBean(ProcessEngine::class.java)

}
