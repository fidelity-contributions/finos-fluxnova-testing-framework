package org.finos.fluxnova.bpm.test.mockito.verify;

import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.test.mockito.DelegateExpressions;
import org.finos.fluxnova.bpm.test.mockito.mock.FluentJavaDelegateMock;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;

public class MockitoVerificationTest {

  private static final String JAVA_DELEGATE = "javaDelegate";

  private final FluentJavaDelegateMock javaDelegate = DelegateExpressions.registerJavaDelegateMock(JAVA_DELEGATE);

  @Mock
  private DelegateExecution delegateExecution;

  @Before
  public void setUp() throws Exception {
    initMocks(this);
  }

  @Test
  public void shouldVerifyExecuteCalled() throws Exception {
    javaDelegate.execute(delegateExecution);

    DelegateExpressions.verifyJavaDelegateMock(JAVA_DELEGATE).executed();
  }

  @Test
  public void shouldVerifyExecuteCalledTwice() throws Exception {
    javaDelegate.execute(delegateExecution);
    javaDelegate.execute(delegateExecution);

    DelegateExpressions.verifyJavaDelegateMock(JAVA_DELEGATE).executed(times(2));
  }

  @Test
  public void shouldVerifyExecuteNotCalled() throws Exception {
    DelegateExpressions.verifyJavaDelegateMock(JAVA_DELEGATE).executedNever();
  }
}
