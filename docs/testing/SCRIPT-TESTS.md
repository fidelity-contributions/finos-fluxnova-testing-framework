# Introduction
The framework provides support in testing scripts that are executed in a bpmn model. This includes external and inline scripts.
The framework is built on top of the Spock testing framework (https://spockframework.org/spock/docs/1.1-rc-1/all_in_one.html) and provides the script engines used in the fluxnova core environment to evaluate the scripts; and helper methods to ease the script execution, process variables mocking, stubbing, date mocking, etc.
This framework abstracts away the difference between writing tests for Groovy and JavaScript based script tasks from the BPMN developers because they don't need to understand the complexity behind mocking languages.
## First steps
The first step in writing tests for the scripts is to create a maven project and consume the finos-fluxnova-testing-framework as a dependency.
For an external script to be tested the script needs to be added to the maven project.
For an inline script to be tested the bpmn file needs to be added to the maven project.
The files will need to be added to the `/src/test/resources directory` in the project, so they are picked up by the tests.
Scripts written in groovy and javascript are both supported with the required engine used in the test to evaluate.

A Spock test is written in Groovy it's declaration looks as so:
```groovy
def 'testing script called multiply.js'() {
}
```

## ScriptTestSpecification Class
The superclass org.finos.fluxnova.bpm.test.scripting.ScriptTestSpecification contains the majority of helper methods required to write the script tests. You must extend this class in your Spock test class. To do so please:
- Create a new Groovy class (if not already created)
- In this Groovy class, extend the ScriptTestSpecification class
- Create a test class
- Extend the parent class into the test class.

Please see examples below for set up.
Example Specification class:
```groovy
import org.finos.fluxnova.bpm.test.scripting.ScriptTestSpecification

class ScriptExampleTest extends ScriptTestSpecification { ... }
```

### Spock Assertions
Spock takes a similar approach such as other testing frameworks. We usually start with a setup, when, then, where(optional) approach. We give the premise (setup), execute the expected events (when) and assert the output (then).
Basic example of a Spock Test
```groovy
def 'testing script called multiply.js'() {

setup: 'before test'
	initializeEngine(ScriptEngineType.GROOVY)
	// some mocking or other operations needed before test (See Helper Methods and Sample Tests sections)
when: 'the script invocation'
	// if external script
	executeExternalScript('scriptFileName')
	// else if inline script
	executeInlineScript('activityIdOfScriptTask', 'bpmnFileName')
	// any other operation eg retrieving evaluated variables after script execution (See Helper Methods and Sample Tests sections)
then: 'assertions'
// eg if you want to check that execution.setVariable('someVariable', 'test') was called once
	1 * execution.setVariable('someVariable', 'test')

}
```
### Verify method a specific amount of times
To verify methods are called a certain amount of times we can prefix the assertion with
1 * execution.setVariable('someVariable', 'test') to assert it was called 1 time. See the example below.
```groovy
def 'testing script called multiply.js'() {

setup: 'before test'
	initializeEngine(ScriptEngineType.GROOVY)
when: 'the script invocation'
	executeInlineScript('activityIdOfScriptTask', 'bpmnFileName')
then: 'assertions'

// eg if you want to check that execution.setVariable('someVariable', 'test') was called once
	1 * execution.setVariable('someVariable', 'test')
}
```

### Verify against data tables
We can verify assertions against multiple data tables easily by using the below syntax. This is useful when we have many points of data. The first line of the table, called the table header, declares the data variables. The subsequent lines, called table rows, hold the corresponding values. For each row, the feature method will get executed once; we call this an iteration of the method. If an iteration fails, the remaining iterations will nevertheless be executed. All failures will be reported.
```groovy
def "maximum of two numbers"(int a, int b, int c) {
    expect:
        Math.max(a, b) == c
    where:
        a | b | c
        1 | 3 | 3
        7 | 4 | 7
        0 | 0 | 0
  }
}
```

### Verify Exceptions
We can verify exceptions have been thrown in the testing framework by using verifyThrownException. This is contained within ScriptTestSpecification.groovy file. There are two signatures to verifyThrownException, one which verifies the closure and exception, and another which verifies the closure, exception and error message.
```groovy
def verifyThrownException(closure, exception) {  
    try {  
        closure.call()  
        throw new ScriptTestException('No exception thrown')  
    } catch (ScriptException ex) {  
        assertException(ex, exception, null)  
    }  
}  
```

```groovy
def verifyThrownException(closure, exception, message) {  
    try {  
        closure.call()  
        throw new ScriptTestException('No exception thrown')  
    } catch (ScriptException ex) {  
        assertException(ex, exception, message)  
    }  
}
```
Usage of verifyThrownException

```groovy
//simple usage without message verification
def 'Test external script - error'() {  
    setup: 'Setup Mocks and binding'  
        initializeEngine(ScriptEngineType.JAVASCRIPT)  
    when: 'Execute Script'  
        verifyThrownException({  
            executeExternalScript('test-error-script.js')  
        }, BpmnError.class);  
    then: ''  
}
```
```groovy
//more complicated usage with message verification
def 'extract_client_info.js - negative' (eventPayload) {  
    setup: 'Setup Mocks and binding'  
        initializeEngine(ScriptEngineType.JAVASCRIPT)  
        def execution = getExecution()  
        mockExecutionAny('getProcessInstanceId', 'PROC_ID')  
  
        stubVariable('eventPayload', JsonOutput.toJson(eventPayload))  
    when: 'Execute Script'  
        verifyThrownException({  
            executeExternalScript('extract_client_info.js')  
        }, BpmnError.class, 'eventPayload execution variable is REQUIRED')  
    then: ''  
        1 * execution.setVariable('errorMessage', 'eventPayload execution variable is REQUIRED')  
    where: ''  
        eventPayload || x  
        null              || 'done'  
        ''                || 'done'  
}
```
### Helper Methods:
initializeEngine(ScriptEngineType scriptEngineType):
This method needs to be called to initialise the script engine. A parameter of type org.finos.fluxnova.bpm.test.scripting.ScriptEngineType needs to be passed in this method for the engine to determine which engine type to initialise i.e. JAVASCRIPT or GROOVY. This method should be called inside the setup step of the test.

For Example: initializeEngine(ScriptEngineType.JAVASCRIPT) or initializeEngine(ScriptEngineType.GROOVY)

getExecution():
This method retrieve the Execution to allow test assertions to be performed on it. This method should be called inside the setup step and utilised inside the then step of the test.

For Example:
```groovy
def execution = getExecution()
//getExecution() offers 3 different methods to be used to perform the assertions.

setVariable(String var1, Object var2)
getVariable(String var1)
hasVariable(String var1)
```

getConnector():
This method retrieve the Connector to allow test assertions to be performed on it. This method should be called inside the setup step and utilised inside the then step of the test.

For Example:
```groovy
def connector = getConnector()
//getConnector() offers 3 different methods to be used to perform the assertions.

setVariable(String var1, Object var2)
getVariable(String var1)
hasVariable(String var1)
```

getEngine():
This method returns the engine which allows custom configurations. The object engine has its value assigned as a part of the execution of initializeEngine(ScriptEngineType scriptEngineType) method.

executeExternalScript(scriptName):
This method takes a script file name as an argument and passes it to the engine for execution. The file name should be passed in as a string. This method should be called inside the when step of the test.
For Example:
```groovy
def evalResult = executeExternalScript('FileName')
```
### Executing Scripts
executeInlineScript(activityId, bpmnFileName):
This method takes a bpmn file name and activityId of the script task to be tested as an arguments and passes it to the engine for execution. Both input arguments have to be passed in as a string. This method should be called inside the when step of the test.

For Example:
```groovy
def evalResult = executeInlineScript('Activity_123', 'FileName')
```
executeInlineScript(activityId, bpmnFileName, event)
This method provides functionality to test an inline script that is part of an execution listener. It takes in the bpmn file name, activityId, and event of the script task to be tested as arguments and passes them to the engine for execution. All input arguments have to be passed in as strings. This method should be called inside the when step of the test.
For Example:
```groovy
def evalResult = executeInlineScript('Activity_123', 'FileName', 'start')
```

### Mocking Execution Object
mockExecutionGetVariable(variables):
This method takes a KeyValue mapping with key being the variable name and value being the variable value. This method should be called inside the setup step of the test.

Use mockExecutionGetVariable when a variable is retrieved from the execution context.
For Example:
Script calls: execution.getVariable('number')
```groovy
def variables = [number: 1]
mockExecutionGetVariable(variables)
```

stubVariable(inputName, variable):
This method takes two input arguments. The first argument (inputName) should be a string and second argument (variable) should be a string or a KeyValue mapping with key being the variable name and value being the variable value. This method performs add the variable mapping to the global mapper inside the script engine. This method should be called inside the setup step of the test.

Use stubVariable for variables that need mocking outside of the execution context. Example: values for global variables:
```groovy
stubVariable('modelName', 'HelloModel')
```
OR
```groovy
def scriptsGlobal = ['PARITY_MODEL_ENV': 'TESTING']
stubVariable('scriptGlobals', scriptsGlobal)
```
#### IMPORTANT
Mocking variables: When to use mockExecutionGetVariable(variables) or stubVariable?
When a variable is retrieved from the execution context, the mockExecutionGetVariable method should be called with a mapping of all the variables to mock.

stubVariable(inputName, variable)
For variables that need mocking outside of the execution context (eg global variables), you should use the stubVariable method instead.

In the following example, we can see the usage of both mockExecutionGetVariable and stubVariable. When hello_world.groovy is called, the 'first' and 'second' variables in the script are combined and set as the 'world' variable. We can see this verified in assertions of the script. This is an example of mocking a variable in the execution context with mockExecutionGetVariable. Further, we can see that mocking stubVariable is not used in the execution anywhere, and is just asserted at the end of the script without any usage in the execution context.
```groovy
def 'galaxy_test_external_groovy'() {
setup: 'initializing mocks and stubs'
	initializeEngine(ScriptEngineType.GROOVY)
	def variables = [
			'first': 'Hello',
			'second': 'World!'
	]
	mockExecutionGetVariable(variables)
	def scriptGlobalsStub = [
			'planet': 'Earth'
	]
	stubVariable('scriptGlobals', scriptGlobalsStub)
	mockExecutionAny('getProcessInstanceId', 'Galaxy_1')
	def execution = getExecution()
when: 'executing the script'
	executeExternalScript('bpmn/scripts/hello_world.groovy')
	def actualGalaxyVariable = getActualExecutionVariable('galaxy')
then: 'assertions'
	1 * execution.setVariable('world', 'Hello World!')
	1 * execution.setVariable('planet', 'Earth')
	assert actualGalaxyVariable.id == 'Galaxy_1'
	assert actualGalaxyVariable.planet == 'someFarAwayPlanet'
}
```

#### mockExecutionAny(method, response):
This method allows mocking of any function in execution and returns a required response back. It takes two input arguments. The first argument (method) should be a string and second argument (response) could be a string or a KeyValue pair. This method should be called inside the setup step of the test.
For Example:
When a script calls method under the execution object like so:
```groovy
execution.getCurrentActivityName()
```
The return value can be mocked as so:
```groovy
mockExecutionAny('getCurrentActivityName', 'wi-set-env-variables')
```
mockExecutionAny(parentMethod, childMethod, response):
This method allows mocking of a child method called by its parent method in the execution and returns a required response back. It takes three input arguments. The first argument (parentMethod) and second argument (child method) should be strings and the third argument (response) could be a string or a KeyValue pair. This method should be called inside the setup step of the test.

For Example:
When a script calls method under the execution object like so:
```groovy
execution.getProcessDefinition().getKey()
```
The return value can be mocked as so:
```groovy
mockExecutionAny('getProcessDefinition', 'getKey', 'MyKey')
```

#### Setting variables to mock using files
Two helper methods are available in VariableHelpers for reading JSON files from mocks and using them as variables in tests:

- fileToMap(String fileName): reads json and returns a Map
- fileToSpinJson(String fileName): reads json and returns a SpinJsonNode

File location rules:

- Place JSON files in src/test/resources/mocks
- Example full path: src/test/resources/mocks/get_secrets_map.json
- Pass only the file name to the helper method (example: get_secrets_map.json)
- Do not pass an absolute path or src/test/resources prefix

The helpers resolve files from classpath:mocks/<fileName>, so the mocks folder must be available on the test classpath.

Example usage in a script test setup:

```groovy
import static org.finos.fluxnova.bpm.test.helpers.VariableHelpers.fileToMap
import static org.finos.fluxnova.bpm.test.helpers.VariableHelpers.fileToSpinJson

def payloadAsMap = fileToMap('get_secrets_map.json')
stubVariable('payloadMap', payloadAsMap)

def payloadAsSpin = fileToSpinJson('get_secrets_map.json')
stubVariable('payloadJson', payloadAsSpin)
```
#### getActualExecutionVariable(variable):
This method retrieves an evaluated variable from the engine after script execution. The returned value could be a string or an object. This method should be called inside the when step of the test.
For Example:
```groovy
def actualAuthorizationPayload = getActualExecutionVariable('authorizationPayload')
```

### Mocking Connector Object
mockConnectorGetVariable(variables):
This method takes a KeyValue mapping with key being the variable name and value being the variable value. This method should be called inside the setup step of the test.
Use mockConnectorGetVariable when a variable is retrieved from the connector object.
For Example:
Script calls: connector.getVariable('number')
```groovy
def variables = [number: 1]
```

mockConnectorGetVariable(variables)
getActualConnectorVariable(variable):
This method retrieves an evaluated variable from the engine after script execution. The returned value could be a string or an object. This method should be called inside the when step of the test.
For Example:
```groovy
def httpUrl = getActualConnectorVariable('httpUrl')
```

### Exception Assertions
verifyThrownException(closure, exception):
This method accepts two arguments. The first argument (closure) should be a response from the method executeExternalScript('FileName') and the second argument (exception) should be an exception class. This method is to test the negative path and should be called inside the when step of the test. The script execution method must be passed in a closure as shown below.
For Example:
```groovy
verifyThrownException({
    executeExternalScript('FileName')
}, Exception.class)
```

verifyThrownException(closure, exception, message):
This method is the overloading method of the above method with one extra argument (message). This argument could be passed as a string as its just an error message with a bit more details about the error.
For Example:
```groovy
verifyThrownException({
    executeExternalScript('FileName')
}, Exception.class,'An Error has occurred while executing abc.')
```

### Date Mocking
mockDate(year, month, day, hour, minutes):
This method is to mock the date inside the engine. It accepts five arguments and all five arguments should be passed as a string. Characters length of each arguments is shown below:

|Argument |Length |Example|
|-------- |------ |-------|
|year	  | 4	  | '2020'|
|month	  | 2	  | '06'  |
|day	  | 2	  | '02'  |
|hour	  | 2	  | '07'  |
|minutes  | 2	  | '25'  |
This method should be called inside the setup step of the test.
For Example:
```groovy
mockDate('2020', '06', '02', '07', '25')
```
mockDate(year, month, day):
This method is the overloading method of the above method with two fewer arguments.
For Example:
```groovy
mockDate('2020', '06', '02')
```

mockDate(hour, minutes):
This method is also the overloading method of the above method with three fewer arguments.
For Example:
```groovy
mockDate('07', '25')
```

### Sonar Integration
The framework supports sonar integration through the bpm-coverage-collection plugin.
This plugin collects coverage for all scripts in the project and allows for SCA to be executed on scripts.
For more details see: [Coverage Reporting](COVERAGE-REPORTING.md)