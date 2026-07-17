package unit

connector.setVariable('foo', 'bar')
def result = connector.getVariable('foo')
return result