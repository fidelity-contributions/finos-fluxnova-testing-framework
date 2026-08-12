package org.finos.fluxnova.bpm.test

import org.finos.fluxnova.bpm.test.scripting.ScriptEngineType
import org.finos.fluxnova.bpm.test.scripting.ScriptTestSpecification
import org.finos.fluxnova.bpm.engine.delegate.BpmnError

import javax.script.ScriptException

class CarPartsOrderScriptsTest extends ScriptTestSpecification {

    def 'getCarPartDetails_RegularCar'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'carAge': 1
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript("GoToPartSearch", "CarParts_Order.bpmn")
        then:
            1 * execution.setVariable('searchUrl', 'https://carparts.com')
            1 * execution.setVariable('isVintage', false)
    }

    def 'getCarPartDetails_VintageCar'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'carAge': 31
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript("GoToPartSearch", "CarParts_Order.bpmn")
        then:
            1 * execution.setVariable('searchUrl', 'https://vintagecarparts.com')
            1 * execution.setVariable('isVintage', true)
    }

    def 'handleCarLookupResponse_noError'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'isVintage': true,
                    'carMakeModel': 'Ford Mustang'
            ]
            mockExecutionGetVariable(variables)
        when:
            executeExternalScript("car-lookup.groovy")
        then:
            noExceptionThrown()
            getActualExecutionVariable('carMakeModel') == 'Ford Mustang'
    }

    def 'handleCarLookupResponse_throwsBpmnError_whenCarModelMissing'(carMakeModelValue) {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = ['isVintage': true, 'carMakeModel': carMakeModelValue]
            mockExecutionGetVariable(variables)
        when:
            executeExternalScript("car-lookup.groovy")
        then:
            def ex = thrown(ScriptException)
            def bpmnError = ex.cause?.cause as BpmnError
            bpmnError != null
            bpmnError.errorCode == '501'
            bpmnError.message == 'Unable to retrieve car make/model from API call!'
        where:
            carMakeModelValue << [null, '']
    }

    def 'ExceptionHandling'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
        when:
            executeInlineScript("ExceptionHandling", "CarParts_Order.bpmn")
        then:
            1 * execution.setVariable('result', 'fail')
    }
}
