package org.finos.fluxnova.bpm.test

import org.finos.fluxnova.bpm.test.scripting.ScriptEngineType
import org.finos.fluxnova.bpm.test.scripting.ScriptTestSpecification

class FinanceParallelLoanReviewScriptsTest extends ScriptTestSpecification {
    def 'creditCheck_Passed'() {
        setup:
            initializeEngine(ScriptEngineType.JAVASCRIPT)
            mockExecutionGetVariable([
                    'payment_failures':0,
                    'credit_limits_breached': 'false',
                    'account_status': 'current'
            ])
        when:
            executeInlineScript('Task_CreditCheck', 'FinanceParallelLoanReview.bpmn')
        then:
            1 * execution.setVariable('credit_check_status', 'PASSED')
    }

    def 'creditCheck_Failed'() {
        setup:
            initializeEngine(ScriptEngineType.JAVASCRIPT)
            mockExecutionGetVariable([
                    'payment_failures':2,
                    'credit_limits_breached': 'false',
                    'account_status': 'current'
            ])
        when:
            executeInlineScript('Task_CreditCheck', 'FinanceParallelLoanReview.bpmn')
        then:
            1 * execution.setVariable('credit_check_status', 'FAILED')
    }

    def 'riskScore_Low'() {
        setup:
            initializeEngine(ScriptEngineType.JAVASCRIPT)
            mockExecutionGetVariable(['risk_score':8])
        when:
            executeInlineScript('Task_RiskScore', 'FinanceParallelLoanReview.bpmn')
        then:
            1 * execution.setVariable('risk_level', 'LOW')
    }

    def 'riskScore_High'() {
        setup:
            initializeEngine(ScriptEngineType.JAVASCRIPT)
            mockExecutionGetVariable(['risk_score':3])
        when:
            executeInlineScript('Task_RiskScore', 'FinanceParallelLoanReview.bpmn')
        then:
            1 * execution.setVariable('risk_level', 'HIGH')
    }

    def 'finalDecision_Approved'() {
        setup:
            initializeEngine(ScriptEngineType.JAVASCRIPT)
            mockExecutionGetVariable([
                    'credit_check_status': 'PASSED',
                    'risk_level': 'LOW'
            ])
        when:
            executeInlineScript('Task_FinalDecision', 'FinanceParallelLoanReview.bpmn')
        then:
            1 * execution.setVariable('LOAN_STATUS', 'APPROVED')
    }

    def 'finalDecision_Rejected'() {
        setup:
            initializeEngine(ScriptEngineType.JAVASCRIPT)
            mockExecutionGetVariable([
                    'credit_check_status': 'FAILED',
                    'risk_level': 'LOW'
            ])
        when:
            executeInlineScript('Task_FinalDecision', 'FinanceParallelLoanReview.bpmn')
        then:
            1 * execution.setVariable('LOAN_STATUS', 'REJECTED')
    }
}
