function mockVariables() {
    var first = execution.getVariable('first')
    var second = execution.getVariable('second')
    var helloWorld = first + ' ' + second
    execution.setVariable('result', helloWorld)
    execution.setVariable('process', execution.getProcessInstanceId())
    return segment
}

mockVariables()