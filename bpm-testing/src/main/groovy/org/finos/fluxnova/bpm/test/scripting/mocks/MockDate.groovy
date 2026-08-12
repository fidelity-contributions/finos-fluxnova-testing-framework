package org.finos.fluxnova.bpm.test.scripting.mocks

class MockDate extends Date {
    MockDate(long time) {
        super(time)
    }

    static def getMockDateJSScript(time) {
        return """
            class MockDate extends Date {
                constructor() {
                    super(${time})
                }
            }
            
            Date = MockDate;
            
            Date.now = function() {
                return (new MockDate().getTime());
            }
        """
    }
}
