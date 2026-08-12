package org.finos.fluxnova.bpm.test.util;

import org.finos.fluxnova.bpm.test.domain.process_variables.ProcessVariable;
import org.finos.fluxnova.bpm.test.domain.process_variables.ProcessVariableType;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProcessVariablesUtil {

    private ProcessVariablesUtil(){}

    public static void setVariableInMockContext(Map<String, List<ProcessVariable>> processVariables, ProcessVariable processVariable, String elementId) {
        List<ProcessVariable> processVariablesForClass = getProcessVariablesForElement(processVariables, elementId);
        processVariablesForClass.add(processVariable);
        processVariables.put(elementId, processVariablesForClass);
    }

    public static void setVariablesInExecutionContext(DelegateExecution delegateExecution, List<ProcessVariable> processVariables) {
        for (ProcessVariable processVariable : processVariables) {
            if (ProcessVariableType.GLOBAL == processVariable.type()) {
                delegateExecution.setVariable(processVariable.variableName(), processVariable.variableValue());
            } else {
                delegateExecution.setVariableLocal(processVariable.variableName(), processVariable.variableValue());
            }
        }
    }

    public static List<ProcessVariable> getProcessVariablesForElement(Map<String, List<ProcessVariable>> processVariables, String elementId) {
        return processVariables.get(elementId) != null ? processVariables.get(elementId) : new ArrayList<>();
    }
}
