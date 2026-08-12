package org.finos.fluxnova.bpm.test.mocks.delegates;

import org.finos.fluxnova.bpm.test.domain.process_variables.ProcessVariable;
import org.finos.fluxnova.bpm.test.domain.process_variables.ProcessVariableType;
import org.finos.fluxnova.bpm.test.domain.process_variables.ProcessVariables;
import org.finos.fluxnova.bpm.test.example.delegates.ExampleOneDelegate;
import org.finos.fluxnova.bpm.test.example.delegates.ExampleTwoDelegate;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MockedDelegateByActivityIdTest {

    @Test
    void testSetVariables_updatesProcessVariablesAsExpected() {
        ExampleOneDelegate exampleDelegate = mock(ExampleOneDelegate.class);
        MockedDelegateInstance<JavaDelegate> mockedDelegateInstance = new MockedDelegateInstance<>(exampleDelegate);
        MockedDelegateByActivityId<?> m = new MockedDelegateByActivityId<>(mockedDelegateInstance, "activityId", new ProcessVariables())
                .setVariable("testName", "testValue")
                .setVariable("otherName", "otherValue")
                .setLocalVariable("testLocalName", "testLocalValue")
                .setLocalVariable("otherLocalName", "otherLocalValue");

        List<ProcessVariable> processVariables = m.processVariables.processVariables().get("activityId");
        assertEquals("testName", processVariables.get(0).variableName());
        assertEquals("testValue", processVariables.get(0).variableValue());
        assertEquals(ProcessVariableType.GLOBAL, processVariables.get(0).type());
        assertEquals("otherName", processVariables.get(1).variableName());
        assertEquals("otherValue", processVariables.get(1).variableValue());
        assertEquals(ProcessVariableType.GLOBAL, processVariables.get(1).type());
        assertEquals("testLocalName", processVariables.get(2).variableName());
        assertEquals("testLocalValue", processVariables.get(2).variableValue());
        assertEquals(ProcessVariableType.LOCAL, processVariables.get(2).type());
        assertEquals("otherLocalName", processVariables.get(3).variableName());
        assertEquals("otherLocalValue", processVariables.get(3).variableValue());
        assertEquals(ProcessVariableType.LOCAL, processVariables.get(3).type());
    }

    @Test
    void testDone_returnsMockedDelegateInstance() {
        ExampleTwoDelegate exampleDelegate = mock(ExampleTwoDelegate.class);
        MockedDelegateInstance<JavaDelegate> mockedDelegateInstance = new MockedDelegateInstance<>(exampleDelegate);
        assertNotNull(new MockedDelegateByActivityId<>(mockedDelegateInstance, "activityId", new ProcessVariables())
                .setVariable("testName", "testValue")
                .done());
    }


    @Test
    void testThrowsException_addsExceptionToExceptionsByActivityIdMap() {
        final String activityId = "testActivityId";
        ExampleOneDelegate exampleDelegate = mock(ExampleOneDelegate.class);
        MockedDelegateInstance<ExampleOneDelegate> mockedDelegateInstance = new MockedDelegateInstance<>(exampleDelegate);

        new MockedDelegateByActivityId<>(mockedDelegateInstance, activityId, new ProcessVariables())
                .throwsException(new RuntimeException("Test for " + activityId));

        assertInstanceOf(RuntimeException.class, mockedDelegateInstance.exceptionsByActivityId.get(activityId));
        assertEquals("Test for " + activityId, mockedDelegateInstance.exceptionsByActivityId.get(activityId).getMessage());
    }

    @Test
    void testThrowsException_canSetDifferentExceptionsForDifferentActivities() {
        final String activityIdOne = "testActivityIdOne";
        final String activityIdTwo = "testActivityIdTwo";
        ExampleOneDelegate exampleOneDelegate = mock(ExampleOneDelegate.class);
        MockedDelegateInstance<ExampleOneDelegate> mockedDelegateInstance = new MockedDelegateInstance<>(exampleOneDelegate);

        new MockedDelegateByActivityId<>(mockedDelegateInstance, activityIdOne, new ProcessVariables())
                .throwsException(new RuntimeException("Test for " + activityIdOne));

        assertInstanceOf(RuntimeException.class, mockedDelegateInstance.exceptionsByActivityId.get(activityIdOne));
        assertEquals("Test for "+ activityIdOne, mockedDelegateInstance.exceptionsByActivityId.get(activityIdOne).getMessage());

        new MockedDelegateByActivityId<>(mockedDelegateInstance, activityIdTwo, new ProcessVariables())
                .throwsException(new RuntimeException("Test for " + activityIdTwo));

        assertInstanceOf(RuntimeException.class, mockedDelegateInstance.exceptionsByActivityId.get(activityIdTwo));
        assertEquals("Test for " + activityIdTwo, mockedDelegateInstance.exceptionsByActivityId.get(activityIdTwo).getMessage());

    }

    @Test
    void testNullOutputs_setsLocalVariablesToSetForActivityIdToNull() {
        ExampleOneDelegate exampleDelegate = mock(ExampleOneDelegate.class);
        MockedDelegateInstance<?> instance = new MockedDelegateInstance<>(exampleDelegate);
        MockedDelegateByActivityId<?> byActivityId = new MockedDelegateByActivityId<>(instance, "activityId", new ProcessVariables());
        byActivityId.setVariablesToNull("output1", "output2");
        List<ProcessVariable> activityVars = byActivityId.processVariables.processVariables().get("activityId");
        assertTrue(activityVars.stream().anyMatch(v -> v.variableName().equals("output1") && v.variableValue() == null && v.type() == ProcessVariableType.LOCAL));
        assertTrue(activityVars.stream().anyMatch(v -> v.variableName().equals("output2") && v.variableValue() == null && v.type() == ProcessVariableType.LOCAL));
    }

}
