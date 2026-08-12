package org.finos.fluxnova.bpm.test.plugin.utils;

import org.finos.fluxnova.bpm.model.bpmn.instance.Script;
import org.finos.fluxnova.bpm.model.bpmn.instance.ScriptTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UtilsTest {

    @Mock
    ScriptTask scriptTask;

    @Mock
    Script script;

    @Test
    void calculateCoverage_givesCurrentPercentageAs100() {
        assertEquals(100, Utils.calculateCoverage(10, 10));
    }

    @Test
    void calculateCoverage_givesCurrentPercentageAs45() {
        assertEquals(45.33, Utils.calculateCoverage(75, 34));
    }

    @Test
    void calculateCoverage_givesCurrentPercentageAs32() {
        assertEquals(32.86, Utils.calculateCoverage(70, 23));
    }

    @Test
    void calculateCoverage_givesCurrentPercentageAs0() {
        assertEquals(0.00, Utils.calculateCoverage(70, 0));
    }

    @Test
    void roundTo2Decimal_givesCorrectResultAs0() {
        assertEquals(0.00, Utils.roundTo2Decimal(0.00));
    }

    @Test
    void roundTo2Decimal_givesCorrectResultNoRoundUp() {
        assertEquals(34.34, Utils.roundTo2Decimal(34.343231));
    }

    @Test
    void roundTo2Decimal_givesCorrectResultWithRoundUp() {
        assertEquals(34.35, Utils.roundTo2Decimal(34.348231));
    }

    @Test
    void isScriptValid_scriptPopulatedAndGroovy_returnsTrue() {
        when(scriptTask.getScript()).thenReturn(script);
        when(scriptTask.getScriptFormat()).thenReturn("groovy");
        when(script.getTextContent()).thenReturn("def test=1");
        assertTrue(Utils.isScriptValid(scriptTask));
    }
    @Test
    void isScriptValid_scriptPopulatedAndJS_returnsTrue() {
        when(scriptTask.getScript()).thenReturn(script);
        when(scriptTask.getScriptFormat()).thenReturn("js");
        when(script.getTextContent()).thenReturn("var i=0;");
        assertTrue(Utils.isScriptValid(scriptTask));
    }
    @Test
    void isScriptValid_scriptPopulatedAndJavascript_returnsTrue() {
        when(scriptTask.getScript()).thenReturn(script);
        when(scriptTask.getScriptFormat()).thenReturn("javascript");
        when(script.getTextContent()).thenReturn("var i=0;");
        assertTrue(Utils.isScriptValid(scriptTask));
    }
    @Test
    void isScriptValid_scriptEmptyAndNoFormat_returnsFalse() {
        when(scriptTask.getScript()).thenReturn(script);
        when(script.getTextContent()).thenReturn("");
        assertFalse(Utils.isScriptValid(scriptTask));
    }
    @Test
    void isScriptValid_scriptPopulatedAndInvalidFormat_returnsFalse() {
        when(scriptTask.getScript()).thenReturn(script);
        when(scriptTask.getScriptFormat()).thenReturn("java");
        when(script.getTextContent()).thenReturn("var i=0;");
        assertFalse(Utils.isScriptValid(scriptTask));
    }
}
