package ${groupId};

import org.finos.fluxnova.bpm.test.scripting.ScriptEngineType
import org.finos.fluxnova.bpm.test.scripting.ScriptTestSpecification

class InsuranceClaimScriptsTest extends ScriptTestSpecification {

    def 'receiveClaim_noVariablesProvided_setsAllDefaults'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            mockExecutionGetVariable([:])
        when:
            executeInlineScript("Task_ReceiveClaim", "bpmn/insurance_claim_process.bpmn")
        then:
            1 * execution.setVariable('claimComplete', false)
            1 * execution.setVariable('approvedClaim', false)
            1 * execution.setVariable('claimStatus', 'RECEIVED')
            1 * execution.setVariable('claimAmount', 0)
            1 * execution.setVariable('approvalThreshold', 10000)
    }

    def 'receiveClaim_claimCompleteAlreadySet_doesNotOverride'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'claimComplete': true
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript("Task_ReceiveClaim", "bpmn/insurance_claim_process.bpmn")
        then:
            0 * execution.setVariable('claimComplete', _)
            1 * execution.setVariable('approvedClaim', false)
            1 * execution.setVariable('claimStatus', 'RECEIVED')
            1 * execution.setVariable('claimAmount', 0)
            1 * execution.setVariable('approvalThreshold', 10000)
    }

    def 'receiveClaim_claimStatusAlreadyValidated_doesNotOverride'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'claimStatus': 'VALIDATED'
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript("Task_ReceiveClaim", "bpmn/insurance_claim_process.bpmn")
        then:
            1 * execution.setVariable('claimComplete', false)
            1 * execution.setVariable('approvedClaim', false)
            0 * execution.setVariable('claimStatus', _)
            1 * execution.setVariable('claimAmount', 0)
            1 * execution.setVariable('approvalThreshold', 10000)
    }

    def 'receiveClaim_claimAmountAlreadySet_doesNotOverride'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'claimAmount': 5000
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript("Task_ReceiveClaim", "bpmn/insurance_claim_process.bpmn")
        then:
            1 * execution.setVariable('claimComplete', false)
            1 * execution.setVariable('approvedClaim', false)
            1 * execution.setVariable('claimStatus', 'RECEIVED')
            0 * execution.setVariable('claimAmount', _)
            1 * execution.setVariable('approvalThreshold', 10000)
    }

    def 'receiveClaim_approvalThresholdAlreadySet_doesNotOverride'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'approvalThreshold': 25000
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript("Task_ReceiveClaim", "bpmn/insurance_claim_process.bpmn")
        then:
            1 * execution.setVariable('claimComplete', false)
            1 * execution.setVariable('approvedClaim', false)
            1 * execution.setVariable('claimStatus', 'RECEIVED')
            1 * execution.setVariable('claimAmount', 0)
            0 * execution.setVariable('approvalThreshold', _)
    }

    def 'receiveClaim_allVariablesAlreadySet_setsNothing'() {
        setup:
            initializeEngine(ScriptEngineType.GROOVY)
            def variables = [
                    'claimComplete': true,
                    'approvedClaim': true,
                    'claimStatus': 'VALIDATED',
                    'claimAmount': 5000,
                    'approvalThreshold': 25000
            ]
            mockExecutionGetVariable(variables)
        when:
            executeInlineScript("Task_ReceiveClaim", "bpmn/insurance_claim_process.bpmn")
        then:
            0 * execution.setVariable(_, _)
    }
}
