package org.finos.fluxnova.bpm.test.example.delegates;

import org.finos.fluxnova.bpm.engine.delegate.BpmnError;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;

public class SendSMSDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String phoneNumber = (String) delegateExecution.getVariable("phoneNumber");
        String message = (String) delegateExecution.getVariable("message");

        if (phoneNumber == null || message == null) {
            throw new BpmnError("500", "phoneNumber and message variables are both required");
        }

        delegateExecution.setVariable("smsResult", "success");
    }
}
