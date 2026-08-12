package org.finos.fluxnova.bpm.test.scripting.mocks

import spock.lang.Specification

class MockDateTest extends Specification {

    def 'Groovy MockDate'() {
        setup: 'set epoch'
            def epoch = 1724248800015L
        when: 'create mock date instance'
            def mockDateInstance = new MockDate(epoch)
        then: 'assert date mocked correctly'
            assert 1724248800015 == mockDateInstance.getTime()
    }

    def 'Javascript MockDate'() {
        setup: 'set epoch'
            def epoch = 1724245485
        when: 'get js mock date instance'
            def mockDateJSInstance = MockDate.getMockDateJSScript(epoch)
        then: 'assert date instance correct'
            assert mockDateJSInstance == """
            class MockDate extends Date {
                constructor() {
                    super(1724245485)
                }
            }
            
            Date = MockDate;
            
            Date.now = function() {
                return (new MockDate().getTime());
            }
        """
    }
}
