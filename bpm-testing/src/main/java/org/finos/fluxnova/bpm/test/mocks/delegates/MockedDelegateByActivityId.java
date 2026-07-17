package org.finos.fluxnova.bpm.test.mocks.delegates;

import org.finos.fluxnova.bpm.test.domain.process_variables.ProcessVariable;
import org.finos.fluxnova.bpm.test.domain.process_variables.ProcessVariableType;
import org.finos.fluxnova.bpm.test.domain.process_variables.ProcessVariables;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;

import static org.finos.fluxnova.bpm.test.util.ProcessVariablesUtil.setVariableInMockContext;

public class MockedDelegateByActivityId<T extends JavaDelegate> {

    private final MockedDelegateInstance<T> mockedDelegateInstance;
    private final String activityId;
    protected final ProcessVariables processVariables;

    protected MockedDelegateByActivityId
            (MockedDelegateInstance<T> mockedDelegateInstance, String activityId, ProcessVariables processVariables) {
        this.mockedDelegateInstance = mockedDelegateInstance;
        this.activityId = activityId;
        this.processVariables = processVariables;
    }

    public MockedDelegateByActivityId<T> setVariable(String variableName, Object variableValue) {
        setVariable(variableName, variableValue, ProcessVariableType.GLOBAL);
        return this;
    }

    public MockedDelegateByActivityId<T> setLocalVariable(String variableName, Object variableValue) {
        setVariable(variableName, variableValue, ProcessVariableType.LOCAL);
        return this;
    }

    public MockedDelegateByActivityId<T> throwsException(Exception e) {
        mockedDelegateInstance.exceptionsByActivityId.put(activityId, e);
        return this;
    }

    public MockedDelegateByActivityId<T> setVariablesToNull(String... outputs) {
        for (String output : outputs) {
            setVariable(output, null, ProcessVariableType.LOCAL);
        }
        return this;
    }

    public MockedDelegateInstance<T> done() {
        return this.mockedDelegateInstance;
    }

    private void setVariable(String variableName, Object variableValue, ProcessVariableType type) {
        ProcessVariable processVariable = new ProcessVariable(type, variableName, variableValue);
        setVariableInMockContext(this.processVariables.processVariables(), processVariable, this.activityId);
    }


}
