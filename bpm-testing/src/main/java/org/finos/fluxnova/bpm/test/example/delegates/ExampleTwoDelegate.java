package org.finos.fluxnova.bpm.test.example.delegates;

import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;

public class ExampleTwoDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // some delegate logic
        delegateExecution.setVariable("websiteUrl", "exampleTwoValue");
    }
}
