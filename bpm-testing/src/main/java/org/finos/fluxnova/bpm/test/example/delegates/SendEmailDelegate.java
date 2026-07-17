package org.finos.fluxnova.bpm.test.example.delegates;

import org.finos.fluxnova.bpm.engine.delegate.BpmnError;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;

public class SendEmailDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String email = (String)delegateExecution.getVariable("customerEmail");
        String customerName = (String)delegateExecution.getVariable("customerName");

        if (email == null || customerName == null) {
            throw new BpmnError("500", "Customer name and email are required!");
        }

        delegateExecution.setVariable("emailResult", "success");
    }
}
