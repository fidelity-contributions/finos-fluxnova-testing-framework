## Introduction
This document outlines the high level details of the methods used in process level tests to test the BPMN models.
For process level tests, the main libraries we are using are JUnit and Fluxnova engine.

The BPMN, DMN and external script files and required external scripts are to be present in the project to allow all to be deployed into the in-memory fluxnova engine during test runtime.

### Test Creation
A Java test class should be created per bpmn model. This class must be a subclass of ProcessTestExtension.
A spring boot app is started when a test is run.
Resources are deployed to the in-memory fluxnova engine through setup(pathToBpmn, dependencies...) and must be provided 
as classpath-relative paths.

#### Classpath resources (required)
Process and dependency resources are resolved from the test runtime classpath.
Recommended layout:
- src/test/resources/bpmn/\*.bpmn for BPMN models
- src/test/resources/bpmn/scripts/\*.{groovy,js} for external scripts
- src/test/resources/dmn/\*.dmn for DMN files

If a resource is not on the classpath or the path is incorrect, setup fails with a deployment error.

The test class can inherit the following from the ProcessTestExtension superclass:
setup method - This method must be called to deploy the bpmn under test and any required dependencies (external scripts, dmns, etc)
This must be annotated with the @BeforeAll clause. This method also starts the wiremock server.

```java
setup(String pathToBpmn, String... dependencies);

//Example 1: The bpmn under test has no dependencies and is located in folder bpmn
setup("bpmn/test.bpmn");

//Example 2: The bpmn under test references external scripts called test_input.groovy and test_output.groovy both located in folder scripts
setup("bpmn/test.bpmn", "scripts/test_input.groovy", "scripts/test_output.groovy);
```

teardown method - This method must be called to remove all deployments from the in memory engine to avoid test interference.
This must be annotated with the @AfterAll clause
```java
teardown();
```

Since a spring boot application will be spun up on test execution, the test class needs to be annotated with @SpringBootTest.

### Starting a Process Instance
The runtimeService() can be used to start a process instance under test by using the process definition key:

```java
ProcessInstance instance = runtimeService().createProcessInstanceByKey("<process_instance_key>").execute();
```
Once the process is started the flow will be executed. The test will wait at certain shapes depending if an action is required, for example a message event, external task, etc or if its asynchronous.

If input variables are required for the process, these can be passed using the setVariable() method passing the variable name and value:
```java
ProcessInstance instance = runtimeService().createProcessInstanceByKey("<process_instance_key>")
.setVariable("<variable_name>", "<variable_value>").setVariable("<variable_name>", <variable_value>).execute();
```

### Progressing the Model
During the testing of a model the test can wait at certain shapes when an action is needed for a specific job. Typically these include shapes that are marked as asynchronous, timer events, user tasks and external tasks.

The following can be used to allow the test to proceed:
- For shapes marked as asynchronous and timer events - execute(job("someActivityId"))
- For user tasks - execute(task("someActivityId")
- For external tasks - execute(externalTask("someActivityId"))
- The testing framework contains a helper method which will proceed through all shapes marked as asynchronous until it reaches a shape that requires a manual action, such as a user task.

### Helper Methods
The testing framework contains some helper methods to allow for easier process level testing.

#### Flow Helpers (FlowHelpers.java)
This class contains three static helper methods to aid in progressing through the model without having to stop in certain circumstances:
 - void advanceFlowUntilAction(String processInstanceId)
This method when called will skip on through the model until it reaches a shape where an action is required (eg a message event)
 - void executeJob(int executionTimes, ProcessInstance instance)
This method executes a number of jobs in the process equal to the passed in executionTimes integer variable.
 - void advanceFlowThroughAllUntilAction(String processInstanceId)
This method when called will skip on through all shapes in the model. It includes parallel gateways, where the process splits into 2 or more parallel
flows after an inclusive gateway. The method ensures no shapes are missed before reaching a shape where an action is required.

#### Variable Helpers (VariableHelpers.java)
This class contains static helper methods to aid in:

#### Setting process variables of a certain type
Extracting objects of a certain type from the execution after process has finished to allow for assertions 
on complex objects.
Retrieving Variables from Process, as follows.
This method returns variable of type Map from execution given variable name:
```java 
Map getVariableTypedMap(String variableName, String processInstanceId) 
```
This method returns variable of type List from execution given variable name:
```java
List getVariableTypedList(String variableName, String processInstanceId)
```
This method returns variable of type String from execution given variable name:
```java
String getVariableTypedString(String variableName, String processInstanceId)
```
This method returns variable of type SpinJsonNode from execution given variable name:
```java
SpinJsonNode getVariableTypedJson(String variableName, String processInstanceId)
```

#### Setting variables in process
These helper methods assist in converting a string in Java or a file to either a Map or SpinJsonNode.
These converted entities can then be used as a variable value.
For example, if a variable consists of a SpinJsonNode type this can be used to create that object in the test and set it as a variable:
SpinJsonNode toSpinJsonType(String json).
This method converts String representing json to a SpinJsonNode object

Example:
```java String json = """
String json = "{
    "name": "John",
    "age": "99"
}"

SpinJsonNode person = toSpinJsonType(json);
setVariable("person", person);
```
Map toMapType(String json)
This method converts String representing json to a Map object

Example:
```java
String json = 
"{
    "John" :
    {
        "age": "99"
    }
}"

Map person = toMapType(json);
setVariable("person", person);
```

#### Using a parameter variable file
Two helper methods are available in VariableHelpers for reading JSON files from mocks and using them as variables 
in tests:

- fileToMap(String fileName): reads json and returns a Map
- fileToSpinJson(String fileName): reads json and returns a SpinJsonNode

File location rules:

- Place JSON files in src/test/resources/mocks
- Example full path: src/test/resources/mocks/get_secrets_map.json
- Pass only the file name to the helper method (example: get_secrets_map.json)
- Do not pass an absolute path or src/test/resources prefix

The helpers resolve files from classpath:mocks/<fileName>, so the mocks folder must be available on the test 
classpath.

Example usage in a script test setup:
```java
Map fileToMap(String fileNamePath)

//This method converts a file representing json to a Map object
//Example:
String fileName="johns_data.json"
//Full FilePath=src/test/resources/mocks/johns_data.json
FilContents=
{
    "John" :
    {
        "age": "99"
    }
}

Map johnsData = fileToMap(fileName);
setVariable("person", johnsData);
``` 
```java
SpinJsonNode fileToSpinJson(String fileNamePath)
//This method converts a file representing json to a SpinJsonNode object

//Example:
String fileName="johns_data.json";
//Full FilePath=src/test/resources/mocks/johns_data.json

SpinJsonNode johnsData = fileToSpinJson(fileName);
setVariable("person", johnsData);
```

## Mocking Components
A BPMN model can contain some logic that increases test complexity and decreases testing feasibility especially from a unit testing perspective. To counteract this some components need to be mocked - including all components that make any external connections, require any credentials etc.
All http connectors, delegates and message events should be mocked (and in most cases call activities) to ensure model is tested correctly using this framework.

### Call Activities
In order to mock a call activity, the repositoryService() can be used.
If output variables are required to be set onExecutionAddVariable() can be invoked passing in variables names and values.
Once the mock object is created then it can be deployed using the ProcessEngineRule which can be inherited from the ProcessTestExtension superclass.

Full details on mocking external subprocess can be found here: https://github.com/camunda-community-hub/camunda-platform-7-mockito?tab=readme-ov-file#mocking-of-external-subprocesses
```java 
Deployment mockedCallActivity = FluxnovaMockito.registerCallActivityMock("<called_process_instance_key>").deploy(repositoryService());
fluxnova.manageDeployment(mockedCallActivity);
```
Call activities should be mocked and deployed before the process instance is executed.

### Http Connectors
If http connectors are used in the model then these will need to be mocked. Wiremock (https://github.com/wiremock/wiremock) can be used to achieve this by stubbing out required status codes, responses, etc per request.
A wiremock server is required to be started at a specified port to serve mocked apis. The wiremock server is started in the setup() method and is stopped in the teardown() method.

To stub a response using wiremock use the static method stubFor() - this takes in the http method which accepts url to match, headers with the desired status code and file containing the desired response.

All expected response files should be placed in the __files directory in the test resources package. The wiremock stubbings must always be defined before starting the process instance in the test.

### Message Events
Message events can be triggered using the fluxnova testing library by passing in the message event name along with the process instance id of the main process. Variables can also be set during this mocking.
```java
runtimeService().createMessageCorrelation("<message_event_name>").processInstanceId(instance.getId()).setVariable("<variable_name>", <variable_value>).correlate();
```
### Delegates
Delegates whether applied using an element template or by using the java implementation can be mocked. All delegates used in a model must be mocked otherwise if real invocation of a delegate occurs then its dependencies must be mocked which increases complexity substantially.

Prerequisites:
To mock a delegate a few things must be known:

- Delegate class - The java class belonging to the delegate must be known by the tester to allow a mock to be applied to it. This can be found in the model xml - search for the service task the delegate belongs to and extract the camunda:class attribute.
- Output variables - When a delegate is mocked the desired variables can be set to the discretion of the test. The variable name is required for this, rather than the output label. The variable name to label mapping can be found in the delegate's documentation.

### Steps to mock a delegate:
Add the Delegate java class to the DelegateMocks class as a Bean. This creates a mocked instance of the delegate:
DelegateMocks.java
```java
@Configuration
public class DelegateMocks {

    @Bean
    ExampleOneDelegate exampleOneDelegate() {
        return Mockito.mock(ExampleOneDelegate.class, MockReset.after());
    }

    @Bean
    CarPartsDelegate carPartsDelegate() {
        return Mockito.mock(CarPartsDelegate.class, MockReset.after());
    }

    @Bean
    SendEmailDelegate sendEmailDelegate() {
        return Mockito.mock(SendEmailDelegate.class, MockReset.after());
    }

    @Bean
    // add delegate here
}
```
Autowire the DelegateMocks component into the test class:
OrderTest.java
```java
@SpringBootTest
public class OrderTest {
	@Autowired
	private DelegateMocks delegateMocks;
	...
	// tests
	...
}
```
Pass the mocked instance into the DelegateHelper mock utility.
Set variables as required:
```java
DelegateHelpers
	.mockDelegate(delegateMocks.someDelegate())
	.setVariable("someVariable", "someValue")
.mock();
```
It is possible to mock by activity in the case where the same delegate is used twice in a model with two different mocking behaviours expected:
```java
DelegateHelpers
	.mockDelegate(delegateMocks.exampleOneDelegate())
	.setVariable("value", "outer") // global mock 
	.mockByActivityId("GetDetails")
		.setVariable("value", "first") // specific mock for that activity
		.done()
.mock();
```
### Delegate Mocking Documentation
```java
mockDelegate(JavaDelegate delegate) -> accepts a mocked instance of a Java delegate
setVariable(String variableName, Object variableValue) -> set a variable on delegate execution. Can be called 0..n times
mockByActivityId(String activityId) -> narrows mock down to specific activity. Can be called 0..n times
setVariable(String variableName, Object variableValue) -> set a variable on delegate execution when delegate called on specific activity. Can be called 0..n times
done() -> must be called when finishing mock for activity
mock() -> must be called when mocking of delegate complete
```
### Delegate Mocking Exception Handling
When mocking a delegate (no output variables) and expecting the delegate to throw an exception, you can call the following method:
```java
DelegateHelpers.mockDelegate(delegateMocks.exampleOneDelegate()).throwsException(new RuntimeException("Error Message")).mock();
```
Or throw an exception for a delegate (no output variables) on a particular activity id:
```java
DelegateHelpers.mockDelegate(delegateMocks.exampleOneDelegate()).mockByActivityId("GetDetails").throwsException(new RuntimeException("GetDetails")).done().mock();
```
When throwing an exception in a mocked delegate that has output variables the setVariablesToNull method must be called with all output variable names.
Note: This is required for any exception flows for a delegate that has output fields, otherwise the test will not run as expected.
```java
DelegateHelpers
.mockDelegate(delegateMocks.someDelegate())
.setVariablesToNull("output_variable", "output_variable")
.throwsException(new SomeDelegateException("CACHE_ERROR"))
.mock();
```
#### Additional Notes:
When starting a process where the first shape in the model is a delegate and has asynchronous continuations, the process will automatically kick off without having to step through the model using `execute(job())`. Hence, the entire delegate logic is wrapped within the `assertThrows` since we know the expected behaviour is to throw an exception.

In a case when mocking the same delegate class with two different activity IDs, where one is mocked successfully but the other is throwing an exception, you can create an instance variable outside of the `assertThrows` so the values inside of it can be asserted:
```java
var ref = new Object() {  
    ProcessInstance subprocessInstance = null;  
};
```
The Instance variable has to be wrapped this way because we cannot override a local variable from inside of a lambda expression.

#### Escalation Events
To trigger an escalation subprocess through test code a process instance "modification" is required. This allows for a subprocess to be executed while the main process is running, at anytime.
When the main process is executing it is possible to manually invoke the event subprocess with the escalation start event. This can be achieved by using the createProcessInstanceModification method while passing in the main process instance id and the start event of the subprocess.

In this example the escalation start event has activityId of Event_1dua3ok
```java
runtimeService().createProcessInstanceModification(instance.getId()).startBeforeActivity("Event_1dua3ok").execute();
```
OR
```java
ProcessInstance subprocessInstance = runtimeService().createProcessInstanceModification(instance.getId()).startBeforeActivity("Event_1dua3ok").execute();
```
This can be done as many times as required in a test.

#### Testing Coverage
When a test class is run the coverage is computed for that model. If coverage dips below the threshold the test will fail.
The threshold is set by default as 80%. To modify the threshold pass system property coverageThreshold with the threshold value.
Example:
mvn clean install -DcoverageThreshold=100

After a test run it is possible to see a visual representation of the coverage for that model under target/process-test-coverage/<test-class-name>. This will contain a html file which can be opened in the browser.
For further details on test coverage see the [coverage reporting documentation](COVERAGE-REPORTING.md)

