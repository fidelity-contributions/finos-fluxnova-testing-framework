package org.finos.fluxnova.bpm.test.mockito.delegate;

import org.finos.fluxnova.bpm.engine.ProcessEngine;
import org.finos.fluxnova.bpm.engine.ProcessEngineServices;
import org.finos.fluxnova.bpm.engine.delegate.ProcessEngineServicesAware;

abstract class DelegateFake<T extends DelegateFake> extends VariableScopeFake<T> implements ProcessEngineServicesAware {

  protected final ProcessEngineServicesAwareFake processEngineServicesAwareFake = new ProcessEngineServicesAwareFake();

  @Override
  public ProcessEngineServices getProcessEngineServices() {
    return processEngineServicesAwareFake.getProcessEngineServices();
  }

  public T withProcessEngineServices(ProcessEngineServices processEngineServices) {
    processEngineServicesAwareFake.withProcessEngineServices(processEngineServices);
    return (T) this;
  }


  @Override
  public ProcessEngine getProcessEngine() {
    return processEngineServicesAwareFake.getProcessEngine();
  }

  public T withProcessEngine(ProcessEngine processEngine) {
    processEngineServicesAwareFake.withProcessEngine(processEngine);
    return (T) this;
  }

}
