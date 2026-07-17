package unit

def value = connector.getVariable('myKey')
execution.setVariable("output", value)