package org.finos.fluxnova.bpm.test.helpers;

import org.finos.fluxnova.bpm.test.TestException;
import org.finos.fluxnova.bpm.engine.HistoryService;
import org.finos.fluxnova.bpm.engine.history.HistoricVariableInstance;
import org.finos.fluxnova.bpm.engine.history.HistoricVariableInstanceQuery;
import org.finos.fluxnova.bpm.engine.test.assertions.ProcessEngineTests;
import org.finos.fluxnova.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.finos.fluxnova.spin.json.SpinJsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VariableHelpersTest {

    @Mock
    private HistoryService historyService;

    @Mock
    private HistoricVariableInstance historicVariableInstance;

    @Mock
    private HistoricVariableInstanceQuery historicVariableInstanceQuery;

    @Test
    void getVariableTypedMap_returnsMap() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::historyService).thenReturn(historyService);
            doReturn("variableName").when(historicVariableInstance).getName();
            doReturn(historicVariableInstanceQuery).when(historyService).createHistoricVariableInstanceQuery();
            doReturn(historicVariableInstanceQuery).when(historicVariableInstanceQuery).processInstanceId("processid");
            doReturn(Collections.singletonList(historicVariableInstance)).when(historicVariableInstanceQuery).list();
            Map<String, String> map = new HashMap<>();
            map.put("testKey", "testValue");
            doReturn(map).when(historicVariableInstance).getValue();
            Map<?, ?> outputVariableMap = VariableHelpers.getVariableTypedMap("variableName", "processid");
            assertEquals("testValue", outputVariableMap.get("testKey"));
        }
    }

    @Test
    void getVariableTypedMap_throwsExceptionWhenMistyped() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::historyService).thenReturn(historyService);
            doReturn("variableName").when(historicVariableInstance).getName();
            doReturn(historicVariableInstanceQuery).when(historyService).createHistoricVariableInstanceQuery();
            doReturn(historicVariableInstanceQuery).when(historicVariableInstanceQuery).processInstanceId("processid");
            doReturn(Collections.singletonList(historicVariableInstance)).when(historicVariableInstanceQuery).list();
            String variableValue = "someVariableValue";
            doReturn(variableValue).when(historicVariableInstance).getValue();
            assertThrows(TestException.class, () -> {
                VariableHelpers.getVariableTypedMap("variableName", "processid");
            }, "Variable not of type Map");
        }
    }

    @Test
    void getVariableTypedMap_throwsExceptionWhenVariableNotFound() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::historyService).thenReturn(historyService);
            doReturn("variableName").when(historicVariableInstance).getName();
            doReturn(historicVariableInstanceQuery).when(historyService).createHistoricVariableInstanceQuery();
            doReturn(historicVariableInstanceQuery).when(historicVariableInstanceQuery).processInstanceId("processid");
            doReturn(Collections.singletonList(historicVariableInstance)).when(historicVariableInstanceQuery).list();
            doReturn(null).when(historicVariableInstance).getValue();
            assertThrows(TestException.class, () -> {
                VariableHelpers.getVariableTypedMap("variableName", "processid");
            }, "Variable variableName not found");
        }
    }

    @Test
    void getVariableTypedList_returnsList() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::historyService).thenReturn(historyService);
            doReturn("variableName").when(historicVariableInstance).getName();
            doReturn(historicVariableInstanceQuery).when(historyService).createHistoricVariableInstanceQuery();
            doReturn(historicVariableInstanceQuery).when(historicVariableInstanceQuery).processInstanceId("processid");
            doReturn(Collections.singletonList(historicVariableInstance)).when(historicVariableInstanceQuery).list();
            List<String> list = new ArrayList<>();
            list.add("testValue");
            doReturn(list).when(historicVariableInstance).getValue();
            List<?> outputVariableList = VariableHelpers.getVariableTypedList("variableName", "processid");
            assertEquals("testValue", outputVariableList.get(0));
        }
    }

    @Test
    void getVariableTypedList_throwsExceptionWhenMistyped() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::historyService).thenReturn(historyService);
            doReturn("variableName").when(historicVariableInstance).getName();
            doReturn(historicVariableInstanceQuery).when(historyService).createHistoricVariableInstanceQuery();
            doReturn(historicVariableInstanceQuery).when(historicVariableInstanceQuery).processInstanceId("processid");
            doReturn(Collections.singletonList(historicVariableInstance)).when(historicVariableInstanceQuery).list();
            String variableValue = "someVariableValue";
            doReturn(variableValue).when(historicVariableInstance).getValue();
            assertThrows(TestException.class, () -> {
                VariableHelpers.getVariableTypedList("variableName", "processid");
            }, "Variable not of type List");
        }
    }

    @Test
    void getVariableTypedList_throwsExceptionWhenVariableNotFound() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::historyService).thenReturn(historyService);
            doReturn("variableName").when(historicVariableInstance).getName();
            doReturn(historicVariableInstanceQuery).when(historyService).createHistoricVariableInstanceQuery();
            doReturn(historicVariableInstanceQuery).when(historicVariableInstanceQuery).processInstanceId("processid");
            doReturn(Collections.singletonList(historicVariableInstance)).when(historicVariableInstanceQuery).list();
            doReturn(null).when(historicVariableInstance).getValue();
            assertThrows(TestException.class, () -> {
                VariableHelpers.getVariableTypedList("variableName", "processid");
            }, "Variable variableName not found");
        }
    }

    @Test
    void getVariableTypedString_returnsString() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::historyService).thenReturn(historyService);
            doReturn("variableName").when(historicVariableInstance).getName();
            doReturn(historicVariableInstanceQuery).when(historyService).createHistoricVariableInstanceQuery();
            doReturn(historicVariableInstanceQuery).when(historicVariableInstanceQuery).processInstanceId("processid");
            doReturn(Collections.singletonList(historicVariableInstance)).when(historicVariableInstanceQuery).list();
            String variableValue = "testValue";
            doReturn(variableValue).when(historicVariableInstance).getValue();
            String outputVariable = VariableHelpers.getVariableTypedString("variableName", "processid");
            assertEquals("testValue", outputVariable);
        }
    }

    @Test
    void getVariableTypedString_throwsExceptionWhenMistyped() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::historyService).thenReturn(historyService);
            doReturn("variableName").when(historicVariableInstance).getName();
            doReturn(historicVariableInstanceQuery).when(historyService).createHistoricVariableInstanceQuery();
            doReturn(historicVariableInstanceQuery).when(historicVariableInstanceQuery).processInstanceId("processid");
            doReturn(Collections.singletonList(historicVariableInstance)).when(historicVariableInstanceQuery).list();
            List<String> list = new ArrayList<>();
            list.add("testValue");
            doReturn(list).when(historicVariableInstance).getValue();
            assertThrows(TestException.class, () -> {
                VariableHelpers.getVariableTypedString("variableName", "processid");
            }, "Variable not of type String");
        }
    }

    @Test
    void getVariableTypedString_throwsExceptionWhenVariableNotFound() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::historyService).thenReturn(historyService);
            doReturn("variableName").when(historicVariableInstance).getName();
            doReturn(historicVariableInstanceQuery).when(historyService).createHistoricVariableInstanceQuery();
            doReturn(historicVariableInstanceQuery).when(historicVariableInstanceQuery).processInstanceId("processid");
            doReturn(Collections.singletonList(historicVariableInstance)).when(historicVariableInstanceQuery).list();
            doReturn(null).when(historicVariableInstance).getValue();
            assertThrows(TestException.class, () -> {
                VariableHelpers.getVariableTypedString("variableName", "processid");
            }, "Variable variableName not found");
        }
    }

    @Test
    void getVariableTypedJson_returnsJson() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::historyService).thenReturn(historyService);
            doReturn("variableName").when(historicVariableInstance).getName();
            doReturn(historicVariableInstanceQuery).when(historyService).createHistoricVariableInstanceQuery();
            doReturn(historicVariableInstanceQuery).when(historicVariableInstanceQuery).processInstanceId("processid");
            doReturn(Collections.singletonList(historicVariableInstance)).when(historicVariableInstanceQuery).list();
            String jsonString = """
                        {
                            "name": "John"
                        }
                    """;
            doReturn(jsonString).when(historicVariableInstance).getValue();
            SpinJsonNode outputVariable = VariableHelpers.getVariableTypedJson("variableName", "processid");
            assertNotNull(outputVariable);
            assertEquals("John", outputVariable.prop("name").stringValue());
        }
    }

    @Test
    void getVariableTypedJson_throwsExceptionWhenMistyped() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::historyService).thenReturn(historyService);
            doReturn("variableName").when(historicVariableInstance).getName();
            doReturn(historicVariableInstanceQuery).when(historyService).createHistoricVariableInstanceQuery();
            doReturn(historicVariableInstanceQuery).when(historicVariableInstanceQuery).processInstanceId("processid");
            doReturn(Collections.singletonList(historicVariableInstance)).when(historicVariableInstanceQuery).list();
            String variableValue = "testValue";
            doReturn(variableValue).when(historicVariableInstance).getValue();
            assertThrows(TestException.class, () -> {
                VariableHelpers.getVariableTypedJson("variableName", "processid");
            }, "Variable cannot be cast to Spin");
        }
    }

    @Test
    void getVariableTypedJson_throwsExceptionWhenVariableNotFound() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::historyService).thenReturn(historyService);
            doReturn("variableName").when(historicVariableInstance).getName();
            doReturn(historicVariableInstanceQuery).when(historyService).createHistoricVariableInstanceQuery();
            doReturn(historicVariableInstanceQuery).when(historicVariableInstanceQuery).processInstanceId("processid");
            doReturn(Collections.singletonList(historicVariableInstance)).when(historicVariableInstanceQuery).list();
            doReturn(null).when(historicVariableInstance).getValue();
            assertThrows(TestException.class, () -> {
                VariableHelpers.getVariableTypedJson("variableName", "processid");
            }, "Variable variableName not found");
        }
    }

    @Test
    void getVariableValue_returnsNullWhenNoVariablesFound() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::historyService).thenReturn(historyService);
            doReturn(historicVariableInstanceQuery).when(historyService).createHistoricVariableInstanceQuery();
            doReturn(historicVariableInstanceQuery).when(historicVariableInstanceQuery).processInstanceId("processid");
            when(historicVariableInstanceQuery.list()).thenReturn(Collections.emptyList());
            assertNull(VariableHelpers.getVariableValue("variableName", "processid"));
        }
    }

    @Test
    void getVariableValue_returnsNullWhenNoVariableMatched() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::historyService).thenReturn(historyService);
            doReturn("otherVariableName").when(historicVariableInstance).getName();
            doReturn(historicVariableInstanceQuery).when(historyService).createHistoricVariableInstanceQuery();
            doReturn(historicVariableInstanceQuery).when(historicVariableInstanceQuery).processInstanceId("processid");
            doReturn(Collections.singletonList(historicVariableInstance)).when(historicVariableInstanceQuery).list();
            assertNull(VariableHelpers.getVariableValue("variableName", "processid"));
        }
    }

    @Test
    void toMapType_convertsJsonStringToMap() {
        String jsonString = """
                    {
                        "name": "John"
                    }
                """;
        Map<?, ?> map = VariableHelpers.toMapType(jsonString);
        assertEquals("John", map.get("name"));
    }

    @Test
    void failToFindResource() {
        assertThrows(TestException.class, () -> {
            VariableHelpers.fileToMap("nonExisting.json");
        });
    }

    @Test
    void testFileFoundAndConvertedToMapType() {
        Map<?,?> fileMap = VariableHelpers.fileToMap("transactionInput.json");
        Map<String, Map<?, ?>> nestedMap = new HashMap<>();
        Map<String, Object> bMap = new HashMap<>();
        bMap.put("preApprovedTransaction", true);
        bMap.put("transactionId", "HW111");
        nestedMap.put("DAW", bMap);

        assertNotNull(fileMap);
        assertEquals(nestedMap, fileMap);
    }

    @Test
    void testFileFoundAndConvertedToSpinJSON(){
        SpinJsonNode spin = VariableHelpers.fileToSpinJson("transactionInput.json");

        assertNotNull(spin);
        assertEquals("{\"preApprovedTransaction\":true,\"transactionId\":\"HW111\"}", spin.prop("DAW").toString());
    }
}
