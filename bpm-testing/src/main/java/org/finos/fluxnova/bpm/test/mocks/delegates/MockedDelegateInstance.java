package org.finos.fluxnova.bpm.test.mocks.delegates;

import org.finos.fluxnova.bpm.test.domain.process_variables.ProcessVariable;
import org.finos.fluxnova.bpm.test.domain.process_variables.ProcessVariableType;
import org.finos.fluxnova.bpm.test.domain.process_variables.ProcessVariables;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.finos.fluxnova.bpm.test.util.ProcessVariablesUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

public class MockedDelegateInstance<T extends JavaDelegate> {

    private static final String DEFAULT = "default";
    private static final Logger logger = LoggerFactory.getLogger(MockedDelegateInstance.class);
    private final T mockedInstance;
    protected ProcessVariables delegateProcessVariables;
    protected Map<String, Exception> exceptionsByActivityId = new HashMap<>();

    public MockedDelegateInstance(T mockedInstance) {
        this.mockedInstance = mockedInstance;
        this.delegateProcessVariables = new ProcessVariables();
    }

    public MockedDelegateInstance<T> setVariable(String variableName, Object variableValue) {
        setVariable(variableName, variableValue, ProcessVariableType.GLOBAL);
        return this;
    }

    public MockedDelegateInstance<T> setLocalVariable(String variableName, Object variableValue) {
        setVariable(variableName, variableValue, ProcessVariableType.LOCAL);
        return this;
    }

    public MockedDelegateInstance<T> throwsException(Exception e) {
        exceptionsByActivityId.put(DEFAULT, e);
        return this;
    }

    public MockedDelegateInstance<T> setVariablesToNull(String... outputs) {
        for (String output : outputs) {
            setVariable(output, null, ProcessVariableType.LOCAL);
        }
        return this;
    }

    public MockedDelegateByActivityId<T> mockByActivityId(String activityId) {
        return new MockedDelegateByActivityId<>(this, activityId, this.delegateProcessVariables);
    }

    public T mock() {
        try {
            doAnswer(invocation -> {
                DelegateExecution delegateExecution = invocation.getArgument(0);
                String activityId = delegateExecution.getCurrentActivityId();

                // Always set process variables first
                HashMap<String, List<ProcessVariable>> delegateProcessVariablesMapping = this.delegateProcessVariables.processVariables();
                List<ProcessVariable> defaultProcessVariables =
                        getProcessVariablesForElement(delegateProcessVariablesMapping, DEFAULT);
                setVariablesInExecutionContext(delegateExecution, defaultProcessVariables);

                if (delegateProcessVariablesMapping.containsKey(activityId)) {
                    List<ProcessVariable> processVariablesForElementInstance = delegateProcessVariablesMapping.get(activityId);
                    setVariablesInExecutionContext(delegateExecution, processVariablesForElementInstance);
                }

                // Check for exception after setting variables
                Exception exceptionToThrow = exceptionsByActivityId.getOrDefault(activityId, exceptionsByActivityId.get(DEFAULT));
                if (exceptionToThrow != null) {
                    throw exceptionToThrow;
                }

                return null;
            }).when(mockedInstance).execute(any());
        } catch (Exception e) {
            logger.error("Exception occurred while mocking delegate instance", e);
        }

        return this.mockedInstance;
    }

    private void setVariable(String variableName, Object variableValue, ProcessVariableType type) {
        HashMap<String, List<ProcessVariable>> existingProcessVariables = this.delegateProcessVariables.processVariables();
        ProcessVariable processVariable = new ProcessVariable(type, variableName, variableValue);
        setVariableInMockContext(existingProcessVariables, processVariable, DEFAULT);
    }
}
