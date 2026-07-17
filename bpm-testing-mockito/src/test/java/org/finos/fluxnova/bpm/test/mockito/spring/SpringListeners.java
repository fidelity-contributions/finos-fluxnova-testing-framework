package org.finos.fluxnova.bpm.test.mockito.spring;

import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static org.assertj.core.api.Assertions.fail;

public abstract class SpringListeners implements ExecutionListener {

  @Override
  public void notify(DelegateExecution delegateExecution) throws Exception {
    fail(this.getClass().getSimpleName() + ": not implemented!");
  }

  @Component
  public static class SpringComponentListener extends SpringListeners {};

  @Component("namedComponent")
  public static class SpringNamedComponentListener extends SpringListeners {};

  @Service
  public static class SpringServiceListener extends SpringListeners {};

  @Service("namedService")
  public static class SpringNamedServiceListener extends SpringListeners {};

}
