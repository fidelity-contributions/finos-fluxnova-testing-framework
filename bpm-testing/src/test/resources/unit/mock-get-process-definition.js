var processDefinitionKey = execution.getProcessDefinition().getKey()
var processDefinitionName = execution.getProcessDefinition().getName()

execution.setVariable('name', processDefinitionName)
execution.setVariable('key', processDefinitionKey)