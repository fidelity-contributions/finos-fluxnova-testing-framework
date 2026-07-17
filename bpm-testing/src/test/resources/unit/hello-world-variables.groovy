package unit

def first = execution.getVariable('first')
def second = execution.getVariable('second')
def helloWorld = first + ' ' + second
execution.setVariable('result', helloWorld)
execution.setVariable('process', execution.getProcessInstanceId())
return segment