package org.finos.fluxnova.bpm.test.scripting

import org.finos.fluxnova.bpm.model.bpmn.Bpmn
import org.finos.fluxnova.bpm.model.bpmn.BpmnModelInstance
import org.finos.fluxnova.bpm.model.bpmn.instance.ExtensionElements
import org.finos.fluxnova.bpm.model.bpmn.instance.Script
import org.finos.fluxnova.bpm.model.bpmn.instance.fluxnova.FluxnovaExecutionListener
import org.finos.fluxnova.bpm.model.xml.instance.ModelElementInstance

import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat

class ScriptTestUtils {

    static String getProcessDefinitionKey(bpmnModelInstance) {
        def bpmnDefs = bpmnModelInstance.getDefinitions()
        ModelElementInstance model = bpmnDefs.getUniqueChildElementByType( org.finos.fluxnova.bpm.model.bpmn.instance.Process.class );
        return model.getAttributeValue("id")
    }

    static BpmnModelInstance getBpmnModelInstance(byte[] contents) {
        return new Bpmn().doReadModelFromInputStream(new ByteArrayInputStream(contents))
    }

    static protected def loadResource(String resource) {
        def classLoader = Thread.currentThread().getContextClassLoader()
        try (def is = classLoader.getResourceAsStream(resource);
             def scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
            return scanner.useDelimiter("\\A").next();
        } catch (e) {
            throw new ScriptTestException("Error finding file: ", e)
        }
    }

    static protected def extractModelEntities(activityId, bpmnFileName, eventType) {
        try {
            def bpmnContents = loadResource(bpmnFileName)
            BpmnModelInstance bpmnModelInstance = getBpmnModelInstance(bpmnContents.getBytes("UTF-8"))
            def task = bpmnModelInstance.getModelElementById(activityId)
            def script =
                    (eventType != null) ? getExecutionListenerScript(task, eventType) : task.getChildElementsByType(Script).textContent[0]
            return ['instance': bpmnModelInstance, 'script': script];
        } catch (Exception e) {
            def message = "Error finding script in model: ${bpmnFileName} for activityId: ${activityId}"
            throw new ScriptTestException(message, e)
        }
    }

    static protected def getEpoch(yyyy, m, d, hh, mm) {
        def currentDate = getCurrentDateTime()
        def year = yyyy ?: currentDate.year
        def month = m ?: currentDate.month
        def day = d ?: currentDate.day
        def hour = hh ?: currentDate.hour
        def minutes = mm ?: currentDate.minutes
        def timestamp = "${year}-${month}-${day} ${hour}:${minutes}:00"
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:m:ss", Locale.ENGLISH);
        def date = formatter.parse(timestamp);
        return date.toInstant().toEpochMilli();
    }

    static protected def getFileName(path) {
        def filesPaths = path.split("/")
        return filesPaths[filesPaths.size()-1]
    }

    private static def getCurrentDateTime() {
        def currentDate = new Date(System.currentTimeMillis());
        return [
            'year': getDateComponent(currentDate, 'y'),
            'month': getDateComponent(currentDate, 'MM'),
            'day': getDateComponent(currentDate, 'dd'),
            'hour': getDateComponent(currentDate, 'H'),
            'minutes': getDateComponent(currentDate, 'm')
        ]
    }

    private static def getDateComponent(date, pattern) {
        return new SimpleDateFormat(pattern).format(date)
    }

    private static def getExecutionListenerScript(task, eventType) {
        ExtensionElements extensionElements = task.getExtensionElements();
        Collection<FluxnovaExecutionListener> executionListeners = extensionElements.getChildElementsByType(FluxnovaExecutionListener.class);
        def index = executionListeners.fluxnovaEvent.contains(eventType) ? executionListeners.fluxnovaEvent.indexOf(eventType) : -1
        if (index >= 0) {
            return executionListeners.textContent[index];
        } else {
            throw new ScriptTestException("Listener script with event type " +  eventType + " not found")
        }
    }
}
