package org.finos.fluxnova.bpm.test.framework.scripting

import org.finos.fluxnova.bpm.model.bpmn.BpmnModelInstance
import org.finos.fluxnova.bpm.test.scripting.ScriptTestException
import org.finos.fluxnova.bpm.test.scripting.ScriptTestUtils
import spock.lang.Specification

class ScriptTestUtilsTest extends Specification {

    def 'loadResource - happy path'(filename, expectedResponse) {
        when: 'file is loaded from classpath'
            def response = ScriptTestUtils.loadResource(filename)
        then: 'expected response'
            assert expectedResponse.replace('\r\n', '\n') == response.replace('\r\n', '\n')
        where:
            filename                    || expectedResponse
            'unit/hello-world.js'       || 'function helloWorld() {\n    return \'Hello World from Javascript\'\n}\n\nhelloWorld()'
            'unit/hello-world.groovy'   || 'package unit\n\nreturn \'Hello World from Groovy\'\n'
    }

    def 'loadResource - negative'() {
        when: 'file does not exist on classpath'
            ScriptTestUtils.loadResource('non-existent-file.js')
        then: 'expected exception thrown'
            def e = thrown(ScriptTestException)
            assert e.message == 'Error finding file: '
    }

    def 'extractModelEntities - happy path'(activityId, expectedScript) {
        when:
            def response = ScriptTestUtils.extractModelEntities(activityId, 'unit/script-task.bpmn', null)
        then:
            assert expectedScript == response['script']
            assert response['instance'] instanceof BpmnModelInstance
        where:
            activityId          || expectedScript
            'Activity_1wt5y6k'  || 'function helloWorld() {\n    return \'Hello World from Javascript\'\n}\n\nhelloWorld()'
            'Activity_1xt02pa'  || 'return \'Hello World from Groovy\''
    }

    def 'extractModelEntities - negative'(bpmnFileName) {
        when:
            ScriptTestUtils.extractModelEntities('activityId', bpmnFileName, null)
        then:
            def e = thrown(ScriptTestException)
            assert e.message == 'Error finding script in model: ' + bpmnFileName + ' for activityId: activityId'
        where:
            bpmnFileName               |_
            'unit/script-task.bpmn'    |_
            'unit/not-exists.bpmn'     |_
            'unit/no-script-task.bpmn' |_
    }
}
