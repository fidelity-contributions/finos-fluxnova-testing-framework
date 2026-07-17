package org.finos.fluxnova.bpm.test.mockito.function;


import org.finos.fluxnova.bpm.engine.ProcessEngine;
import org.finos.fluxnova.bpm.engine.ProcessEngineConfiguration;
import org.finos.fluxnova.bpm.engine.impl.ProcessEngineImpl;

import java.util.function.Function;

/**
 * Hides the nasty "getConfiguration from given Engine Hack" in an easy to use
 * function.
 */
public enum GetProcessEngineConfiguration implements Function<ProcessEngine, ProcessEngineConfiguration> {
  INSTANCE;

  @Override
  public ProcessEngineConfiguration apply(final ProcessEngine processEngine) {
    if (!(processEngine instanceof ProcessEngineImpl)) {
      throw new IllegalArgumentException("processEngine must not be null and of type ProcessEngineImpl!");
    }

    return ((ProcessEngineImpl) processEngine).getProcessEngineConfiguration();
  }

}
