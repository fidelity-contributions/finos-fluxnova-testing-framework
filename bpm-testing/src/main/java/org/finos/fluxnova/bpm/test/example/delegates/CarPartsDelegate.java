package org.finos.fluxnova.bpm.test.example.delegates;

import org.finos.fluxnova.bpm.engine.delegate.BpmnError;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;

public class CarPartsDelegate  implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String carRegistration = (String)delegateExecution.getVariable("carRegistration");
        if (carRegistration == null) {
            throw new BpmnError("500", "carRegistration variable is required");
        }

    }
}
