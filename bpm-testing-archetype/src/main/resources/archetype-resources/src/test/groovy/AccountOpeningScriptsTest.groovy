package ${groupId};

import org.finos.fluxnova.bpm.test.scripting.ScriptEngineType
import org.finos.fluxnova.bpm.test.scripting.ScriptTestSpecification

class AccountOpeningScriptsTest extends ScriptTestSpecification {

    def 'validate_input_data_first_review'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'customerName': 'John Doe',
                    'customerEmail': 'jdoe@email.com',
                    'totalAssetValue': 30000
            ]
            mockDate('2026', '04', '01', '10', '0')
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript('Task_ValidateInitialData', 'bpmn/AccountOpening-InvestmentAccount.bpmn')
        then:
            getActualExecutionVariable('applicationId') != null
            getActualExecutionVariable('applicationId') == "APP-" + new Date().getTime()
            getActualExecutionVariable('reviewCount') == 1
    }

    def 'validate_input_data_second_review'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'customerName': 'John Doe',
                    'customerEmail': 'jdoe@email.com',
                    'totalAssetValue': 30000,
                    'applicationId': "APP-1234567890",
                    'reviewCount': 1
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript('Task_ValidateInitialData', 'bpmn/AccountOpening-InvestmentAccount.bpmn')
        then:
            getActualExecutionVariable('applicationId') != null
            getActualExecutionVariable('applicationId') == "APP-1234567890"
            getActualExecutionVariable('reviewCount') == 2
    }

    def 'checkAssetValue_standardAccount'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'totalAssetValue': 30000
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript('Task_CheckAssetValue', 'bpmn/AccountOpening-InvestmentAccount.bpmn')
        then:
            getActualExecutionVariable('accountType') == 'STANDARD'

    }

    def 'checkAssetValue_premiumAccount'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'totalAssetValue': 1000000
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript('Task_CheckAssetValue', 'bpmn/AccountOpening-InvestmentAccount.bpmn')
        then:
            getActualExecutionVariable('accountType') == 'PREMIUM'
            1 * execution.setVariable('applicationStatus', 'VALIDATED')
    }

    def 'logManagerRejection'() {
        def rejectionReason = 'Insufficient asset value'

        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'applicationId': "APP-1234567890",
                    'customerName'   : 'Jane Smith',
                    'rejectionReason': rejectionReason,
                    'approvedBy': 'John Deere'
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript('Task_LogRejection', 'bpmn/AccountOpening-InvestmentAccount.bpmn')
        then:
            1 * execution.setVariable('applicationStatus', 'REJECTED_BY_MANAGER')
            1 * execution.setVariable('missingDataDescription', 'Your premium account application requires additional review. ' + rejectionReason)
    }

    def 'logAccountCreation_Phone_WithoutPrefix'() {

        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'applicationId'  : "APP-1234567890",
                    'customerName'   : 'Jane Smith',
                    'customerEmail'  : 'jsmith@email.com',
                    'accountNumber'  : '123435',
                    'accountType'    : 'STANDARD',
                    'totalAssetValue': 30000,
                    'customerPhone'  : 2815551234
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript('Task_LogAccountCreation', 'bpmn/AccountOpening-InvestmentAccount.bpmn')
        then:
            getActualExecutionVariable('customerPhone') == '+12815551234'
            1 * execution.setVariable("applicationStatus", "ACCOUNT_OPENED");
    }

    def 'logAccountCreation_Phone_WithPrefix'() {

        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'applicationId'  : "APP-1234567890",
                    'customerName'   : 'Jane Smith',
                    'customerEmail'  : 'jsmith@email.com',
                    'accountNumber'  : '123435',
                    'accountType'    : 'STANDARD',
                    'totalAssetValue': 30000,
                    'customerPhone'  : '+12815551234'
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript('Task_LogAccountCreation', 'bpmn/AccountOpening-InvestmentAccount.bpmn')
        then:
            getActualExecutionVariable('customerPhone') == '+12815551234'
            1 * execution.setVariable("applicationStatus", "ACCOUNT_OPENED");
    }

    def 'accountCreateFailedError'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'applicationId'  : "APP-1234567890",
                    'customerName'   : 'Jane Smith'
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript('Task_HandleAccountError', 'bpmn/AccountOpening-InvestmentAccount.bpmn')
        then:
            1 * execution.setVariable("applicationStatus", "ERROR_ACCOUNT_CREATION");

    }

    def 'logSMSError'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'applicationId'  : "APP-1234567890",
                    'customerPhone'   : '+1222567890'
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript('Task_LogSMSError', 'bpmn/AccountOpening-InvestmentAccount.bpmn')
        then:
            1 * execution.setVariable("smsDeliveryFailed", true);

    }
}