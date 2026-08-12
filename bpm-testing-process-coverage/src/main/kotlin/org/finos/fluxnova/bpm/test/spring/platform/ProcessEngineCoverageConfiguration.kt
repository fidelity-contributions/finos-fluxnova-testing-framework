package org.finos.fluxnova.bpm.test.spring.platform

import org.finos.fluxnova.bpm.test.engine.ProcessCoverageConfigurator
import org.finos.fluxnova.bpm.engine.spring.SpringProcessEngineConfiguration
import org.finos.fluxnova.bpm.spring.boot.starter.configuration.Ordering
import org.finos.fluxnova.bpm.spring.boot.starter.configuration.impl.AbstractFluxnovaConfiguration
import org.finos.fluxnova.bpm.test.spring.test.common.ProcessEngineCoverageProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order

/**
 * Configuration enabling process test coverage in the fluxnova process engine.
 */
@Configuration
open class ProcessEngineCoverageConfiguration {

    @Bean
    @Order(Ordering.DEFAULT_ORDER + 1)
    open fun fluxnovaConfiguration() = object : AbstractFluxnovaConfiguration() {
        override fun preInit(processEngineConfiguration: SpringProcessEngineConfiguration) {
            ProcessCoverageConfigurator.initializeProcessCoverageExtensions(processEngineConfiguration)
        }
    }

    @Bean
    @ConditionalOnMissingBean(ProcessEngineCoverageProperties::class)
    open fun defaultCoverageProperties() = ProcessEngineCoverageProperties()

}
