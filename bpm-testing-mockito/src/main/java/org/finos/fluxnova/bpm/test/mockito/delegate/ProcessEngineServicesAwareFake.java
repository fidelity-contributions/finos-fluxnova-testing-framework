package org.finos.fluxnova.bpm.test.mockito.delegate;

import org.finos.fluxnova.bpm.engine.ProcessEngine;
import org.finos.fluxnova.bpm.engine.ProcessEngineServices;
import org.finos.fluxnova.bpm.engine.delegate.ProcessEngineServicesAware;

public class ProcessEngineServicesAwareFake implements ProcessEngineServicesAware {

  private ProcessEngine processEngine;
  private ProcessEngineServices processEngineServices;

  @Override
  public ProcessEngineServices getProcessEngineServices() {
    return processEngineServices;
  }

  public ProcessEngineServicesAwareFake withProcessEngineServices(ProcessEngineServices processEngineServices) {
    this.processEngineServices = processEngineServices;
    return this;
  }

  @Override
  public ProcessEngine getProcessEngine() {
    return processEngine;
  }

  public ProcessEngineServicesAwareFake withProcessEngine(ProcessEngine processEngine) {
    this.processEngine = processEngine;
    return withProcessEngineServices(processEngine);
  }

}
