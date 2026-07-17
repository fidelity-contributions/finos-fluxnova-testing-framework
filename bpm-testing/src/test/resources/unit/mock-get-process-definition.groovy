package unit

def processDefinitionKey = execution.getProcessDefinition().getKey()
def processDefinitionName = execution.getProcessDefinition().getName()

execution.setVariable('name', processDefinitionName)
execution.setVariable('key', processDefinitionKey)