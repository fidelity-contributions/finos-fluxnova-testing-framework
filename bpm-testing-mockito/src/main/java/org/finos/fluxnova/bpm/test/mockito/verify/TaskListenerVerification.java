package org.finos.fluxnova.bpm.test.mockito.verify;

import org.finos.fluxnova.bpm.engine.delegate.DelegateTask;
import org.finos.fluxnova.bpm.engine.delegate.TaskListener;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.verify;

public class TaskListenerVerification extends AbstractMockitoVerification<TaskListener, DelegateTask> {

  public TaskListenerVerification(final TaskListener mock) {
    super(mock, DelegateTask.class);
  }

  @Override
  protected void doVerify(final VerificationMode verificationMode) throws Exception {
    verify(mock, verificationMode).notify(argumentCaptor.capture());
  }

}
