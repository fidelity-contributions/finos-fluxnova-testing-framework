package org.finos.fluxnova.bpm.test.mocks.delegates;

import org.finos.fluxnova.bpm.test.example.delegates.ExampleOneDelegate;
import org.finos.fluxnova.bpm.test.mockito.delegate.DelegateExecutionFake;

public class TestHelper {

    public static void execute(ExampleOneDelegate exampleOneDelegate, DelegateExecutionFake delegateExecution) throws Exception {
        exampleOneDelegate.execute(delegateExecution);
    }
}
