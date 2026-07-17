package org.finos.fluxnova.bpm.test.helpers;

import org.finos.fluxnova.bpm.test.TestException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.finos.fluxnova.bpm.engine.history.HistoricVariableInstance;
import org.finos.fluxnova.spin.json.SpinJsonNode;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.List;
import java.util.Map;

import static org.finos.fluxnova.bpm.engine.test.assertions.ProcessEngineTests.historyService;
import static org.finos.fluxnova.spin.Spin.S;

public class VariableHelpers {

    private VariableHelpers() {
    }

    public static <K, V> Map<K, V> getVariableTypedMap(String variableName, String processInstanceId) {
        Object obj = getVariableValue(variableName, processInstanceId);
        isNull(variableName, obj);
        if (obj instanceof Map<?, ?> map) {
            return (Map<K, V>) map;
        } else {
            throw new TestException("Variable not of type Map");
        }
    }

    public static String getVariableTypedString(String variableName, String processInstanceId) {
        Object obj = getVariableValue(variableName, processInstanceId);
        isNull(variableName, obj);
        if (obj instanceof String s) {
            return s;
        } else {
            throw new TestException("Variable not of type String");
        }
    }

    public static <E> List<E> getVariableTypedList(String variableName, String processInstanceId) {
        Object obj = getVariableValue(variableName, processInstanceId);
        isNull(variableName, obj);
        if (obj instanceof List<?> list) {
            return (List<E>) list;
        } else {
            throw new TestException("Variable not of type List");
        }
    }

    public static SpinJsonNode getVariableTypedJson(String variableName, String processInstanceId) {
        try {
            String json = (String) getVariableValue(variableName, processInstanceId);
            isNull(variableName, json);
            return toSpinJsonType(json);
        } catch (Exception e) {
            throw new TestException("Variable cannot be cast to Spin", e);
        }
    }

    public static Object getVariableValue(String variableName, String processInstanceId) {
        List<HistoricVariableInstance> historicVariables = getExecutionVariables(processInstanceId);
        for (HistoricVariableInstance historicVariableInstance : historicVariables) {
            if (historicVariableInstance.getName().equals(variableName)) {
                return historicVariableInstance.getValue();
            }
        }
        return null;
    }

    public static List<HistoricVariableInstance> getExecutionVariables(String processInstanceId) {
        return historyService()
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
    }

    public static <K, V> Map<K, V> toMapType(String json) {
        return S(json.replace("'", "\"")).mapTo(Map.class);
    }

    public static SpinJsonNode toSpinJsonType(String json) {
        return S(json.replace("'", "\""));
    }

    private static void isNull(String variableName, Object variable) {
        if (variable == null) {
            throw new TestException(String.format("Variable %s not found", variableName));
        }
    }

    public static SpinJsonNode fileToSpinJson(String fileName)  {
        try {
            Resource resource = findResource(fileName);
            return toSpinJsonType(FileUtils.readFileToString(resource.getFile(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }
    }

    public static <K, V> Map<K, V> fileToMap(String fileName) {
        try {
            Resource resource = findResource(fileName);
            return (Map<K, V>) toMapType(FileUtils.readFileToString(resource.getFile(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

    }

    private static Resource findResource(String fileName) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        return resolver.getResource("classpath:mocks/" + fileName);
    }
}
