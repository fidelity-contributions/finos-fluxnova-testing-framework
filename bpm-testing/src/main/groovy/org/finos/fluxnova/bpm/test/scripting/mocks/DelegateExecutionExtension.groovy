package org.finos.fluxnova.bpm.test.scripting.mocks

import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution
import org.finos.fluxnova.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity

interface DelegateExecutionExtension extends DelegateExecution {

    ProcessDefinitionEntity getProcessDefinition();

}
