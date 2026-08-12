package ${groupId};


import org.finos.fluxnova.bpm.test.scripting.ScriptEngineType
import org.finos.fluxnova.bpm.test.scripting.ScriptTestSpecification

class FoodOrderScriptsTest extends ScriptTestSpecification {

    def 'getFamilyMember_johnIsVegan'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'firstName': 'John'
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript("GetFamilyMember", "bpmn/FoodOrder.bpmn")
        then:
            1 * execution.setVariable('isVegan', true)
            1 * execution.setVariable('restaurant', 'Chopped')
    }

    def 'test GetFamilyMember script - John is vegan, sets restaurant to Chopped'() {
        setup: 'Setup mocks and execution'
            initializeEngine(ScriptEngineType.GROOVY)
            def execution = getExecution()
            def variables = [
                    'firstName': 'John',
                    'isWeekend': true
            ]
            mockExecutionGetVariable(variables)
        when: 'Execute the script'
            executeInlineScript('GetFamilyMember', 'bpmn/FoodOrder.bpmn')
        then: 'Verify variables are set correctly'
            1 * execution.setVariable('isVegan', true)
            1 * execution.setVariable('restaurant', 'Chopped')
    }

    def 'getFamilyMember_samIsNotVegan'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'firstName': 'Sam'
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript("GetFamilyMember", "bpmn/FoodOrder.bpmn")
        then:
            1 * execution.setVariable('isVegan', false)
            1 * execution.setVariable('restaurant', 'Chopped')
    }

    def 'getFamilyMember_samIsNotVeganAndItsTheWeekend'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'firstName': 'Sam',
                    'isWeekend': true
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript("GetFamilyMember", "bpmn/FoodOrder.bpmn")
        then:
            1 * execution.setVariable('isVegan', false)
            1 * execution.setVariable('restaurant', 'Pizza Hut')
    }

    def 'error-logger_happyPath' () {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
        when:
            executeExternalScript("bpmn/scripts/error-logger.groovy")
        then:
            1 * execution.setVariable('result', 'fail')
    }

    def 'order-details_happyPathJohn' () {
        setup:
            initializeEngine(ScriptEngineType.JAVASCRIPT)
            def variables = [
                    firstName: 'John',
                    menu: 'vegan',
                    restaurant: 'chopped',
                    websiteUrl: 'chopped.com'
            ]
            mockExecutionGetVariable(variables)
        when:
            executeExternalScript("bpmn/scripts/order-details.js")
            def orderDetails = getActualExecutionVariable('orderDetails')
        then:
            assert orderDetails.prop('name').stringValue() == 'John Doe'
            assert orderDetails.prop('menu').stringValue() == 'vegan'
            assert orderDetails.prop('restaurant').stringValue() == 'chopped'
            assert orderDetails.prop('website').stringValue() == 'chopped.com'
            assert orderDetails.prop('address').stringValue() == '123 Main St, Springfield'
    }

    def 'order-details_happyPathSam' () {
        setup:
         initializeEngine(ScriptEngineType.JAVASCRIPT)
         def variables = [
                 firstName: 'Sam',
                 menu: 'standard',
                 restaurant: 'pizzahut',
                 websiteUrl: 'pizza.com'
            ]
            mockExecutionGetVariable(variables)
        when:
            executeExternalScript("bpmn/scripts/order-details.js")
            def orderDetails = getActualExecutionVariable('orderDetails')
        then:
            assert orderDetails.prop('name').stringValue() == 'Sam Doe'
            assert orderDetails.prop('menu').stringValue() == 'standard'
            assert orderDetails.prop('restaurant').stringValue() == 'pizzahut'
            assert orderDetails.prop('website').stringValue() == 'pizza.com'
            assert orderDetails.prop('address').stringValue() == '456 Elm St, Shelbyville'
    }
}
