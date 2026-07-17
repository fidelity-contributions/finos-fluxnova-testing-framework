package org.finos.fluxnova.bpm.test.mocks.delegates;


import org.finos.fluxnova.bpm.test.domain.process_variables.ProcessVariable;
import org.finos.fluxnova.bpm.test.domain.process_variables.ProcessVariableType;
import org.finos.fluxnova.bpm.test.domain.process_variables.ProcessVariables;
import org.finos.fluxnova.bpm.test.example.delegates.ExampleOneDelegate;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;
import org.finos.fluxnova.bpm.engine.variable.impl.VariableMapImpl;
import org.finos.fluxnova.bpm.test.mockito.delegate.DelegateExecutionFake;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MockedDelegateInstanceTest<T extends JavaDelegate> {

    @Test
    void testSetVariables_updatesProcessVariablesAsExpected() {
        ExampleOneDelegate exampleOneDelegate = mock(ExampleOneDelegate.class);
        MockedDelegateInstance<?> m = new MockedDelegateInstance<>(exampleOneDelegate)
                .setVariable("testName", "testValue")
                .setVariable("otherName", "otherValue")
                .setLocalVariable("testLocalName", "testLocalValue")
                .setLocalVariable("otherLocalName", "otherLocalValue");
        ProcessVariables processVariables = m.delegateProcessVariables;
        assertTrue(processVariables.processVariables().containsKey("default"));
        List<ProcessVariable> defaultProcessVariables = processVariables.processVariables().get("default");
        assertEquals("testName", defaultProcessVariables.get(0).variableName());
        assertEquals("testValue", defaultProcessVariables.get(0).variableValue());
        assertEquals(ProcessVariableType.GLOBAL, defaultProcessVariables.get(0).type());
        assertEquals("otherName", defaultProcessVariables.get(1).variableName());
        assertEquals("otherValue", defaultProcessVariables.get(1).variableValue());
        assertEquals(ProcessVariableType.GLOBAL, defaultProcessVariables.get(1).type());
        assertEquals("testLocalName", defaultProcessVariables.get(2).variableName());
        assertEquals("testLocalValue", defaultProcessVariables.get(2).variableValue());
        assertEquals(ProcessVariableType.LOCAL, defaultProcessVariables.get(2).type());
        assertEquals("otherLocalName", defaultProcessVariables.get(3).variableName());
        assertEquals("otherLocalValue", defaultProcessVariables.get(3).variableValue());
        assertEquals(ProcessVariableType.LOCAL, defaultProcessVariables.get(3).type());
    }

    @Test
    void testMockByActivityId_updatesProcessVariablesAsExpected() {
        MockedDelegateInstance<?> m = new MockedDelegateInstance<>(mock(ExampleOneDelegate.class))
                .setVariable("testName", "testValue")
                .mockByActivityId("testActivityId")
                    .setVariable("activityIdVariable", "activityIdValue")
                    .setVariable("otherActivityIdVariable", "otherActivityIdValue")
                    .done()
                .mockByActivityId("anotherActivityId")
                    .setVariable("someOtherActivityVariable", "someOtherActivityIdValue")
                    .done();

        ProcessVariables processVariables = m.delegateProcessVariables;
        assertTrue(processVariables.processVariables().containsKey("default"));
        assertTrue(processVariables.processVariables().containsKey("testActivityId"));
        List<ProcessVariable> defaultProcessVariables = processVariables.processVariables().get("default");
        assertEquals("testName", defaultProcessVariables.get(0).variableName());
        assertEquals("testValue", defaultProcessVariables.get(0).variableValue());

        List<ProcessVariable> testActivityIdProcessVariables = processVariables.processVariables().get("testActivityId");
        assertEquals("activityIdVariable", testActivityIdProcessVariables.get(0).variableName());
        assertEquals("activityIdValue", testActivityIdProcessVariables.get(0).variableValue());
        assertEquals("otherActivityIdVariable", testActivityIdProcessVariables.get(1).variableName());
        assertEquals("otherActivityIdValue", testActivityIdProcessVariables.get(1).variableValue());

        List<ProcessVariable> anotherActivityIdProcessVariables = processVariables.processVariables().get("anotherActivityId");
        assertEquals("someOtherActivityVariable", anotherActivityIdProcessVariables.get(0).variableName());
        assertEquals("someOtherActivityIdValue", anotherActivityIdProcessVariables.get(0).variableValue());
    }

    @Test
    void testMock_delegateExecutionUpdatedAsExpected() throws Exception {
        DelegateExecutionFake delegateExecutionFake = new DelegateExecutionFake();
        ExampleOneDelegate exampleDelegate = mock(ExampleOneDelegate.class);
        new MockedDelegateInstance<>(exampleDelegate)
                .setVariable("testName", "testValue")
                .setVariable("otherName", "otherValue")
                .setLocalVariable("testLocalName", "testLocalValue")
                .setLocalVariable("otherLocalName", "otherLocalValue")
                .mock();
        TestHelper.execute(exampleDelegate, delegateExecutionFake);
        VariableMapImpl variableMap = delegateExecutionFake.getVariables();
        assertEquals("testValue", variableMap.get("testName"));
        assertEquals("otherValue", variableMap.get("otherName"));
        assertEquals("testLocalValue", variableMap.get("testLocalName"));
        assertEquals("otherLocalValue", variableMap.get("otherLocalName"));
    }

    @Test
    void testMock_delegateExecutionPropertyOverriddenByActivityIdAsExpected() throws Exception {
        DelegateExecutionFake delegateExecutionFake =
                new DelegateExecutionFake().withCurrentActivityId("testActivityId");
        ExampleOneDelegate exampleDelegate = mock(ExampleOneDelegate.class);
        new MockedDelegateInstance<>(exampleDelegate)
                .setVariable("testName", "testValue")
                .setVariable("globalTestName", "globalTestValue")
                .mockByActivityId("testActivityId")
                    .setVariable("activityIdVariable", "activityIdValue")
                    .setVariable("testName", "testValueFromActivity")
                    .done()
                .mockByActivityId("otherActivityId")
                    .setVariable("missingVariable", "missingValue")
                    .done()
                .mock();
        TestHelper.execute(exampleDelegate, delegateExecutionFake);
        VariableMapImpl variableMap = delegateExecutionFake.getVariables();
        assertEquals("testValueFromActivity", variableMap.get("testName"));
        assertEquals("activityIdValue", variableMap.get("activityIdVariable"));
        assertEquals("globalTestValue", variableMap.get("globalTestName"));
        assertNull(variableMap.get("missingVariable"));
    }

    @Test
    void testMock_delegateExecutionPropertyNotOverriddenByActivityIdAsExpected() throws Exception {
        DelegateExecutionFake delegateExecutionFake = new DelegateExecutionFake();
        ExampleOneDelegate exampleDelegate = mock(ExampleOneDelegate.class);
        new MockedDelegateInstance<>(exampleDelegate)
                .setVariable("testName", "testValue")
                .setVariable("globalTestName", "globalTestValue")
                .mockByActivityId("testActivityId")
                    .setVariable("activityIdVariable", "activityIdValue")
                    .setVariable("testName", "testValueFromActivity")
                    .done()
                .mockByActivityId("otherActivityId")
                    .setVariable("missingVariable", "missingValue")
                    .done()
                .mock();
        TestHelper.execute(exampleDelegate, delegateExecutionFake);
        VariableMapImpl variableMap = delegateExecutionFake.getVariables();
        assertEquals("testValue", variableMap.get("testName"));
        assertEquals("globalTestValue", variableMap.get("globalTestName"));
        assertNull(variableMap.get("missingVariable"));
        assertNull(variableMap.get("activityIdVariable"));
    }

    @Test
    void testMock_delegateThrowsDefaultExceptionAsExpected() {
        DelegateExecutionFake delegateExecutionFake = new DelegateExecutionFake();
        ExampleOneDelegate exampleDelegate = mock(ExampleOneDelegate.class);
        new MockedDelegateInstance<>(exampleDelegate)
                .throwsException(new RuntimeException("Default Test?"))
                .mock();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            TestHelper.execute(exampleDelegate, delegateExecutionFake);
        });
        assertEquals("Default Test?", exception.getMessage());
    }

    @Test
    void testMock_delegateThrowsExceptionPerActivityIdAsExpected() {
        DelegateExecutionFake delegateExecutionFake = new DelegateExecutionFake()
                .withCurrentActivityId("testActivityId");

        ExampleOneDelegate exampleDelegate = mock(ExampleOneDelegate.class);
        new MockedDelegateInstance<>(exampleDelegate)
            .setVariable("testName", "testValue")
            .throwsException(new RuntimeException("Test for activityId"))
            .mock();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            TestHelper.execute(exampleDelegate, delegateExecutionFake);
        });
        assertEquals("Test for activityId", exception.getMessage());
    }


}
