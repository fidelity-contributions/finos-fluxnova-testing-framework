# REPLACE_ME!! This is your generated BPM testing consumer project

This project contains the required dependencies to create and run both script level tests and process level tests.
It also includes some sample tests.

## Tests

### Script Tests

The script level tests are located under the groovy test package with the external and inline scripts under test living in the resources folder.
These tests are written in the Spock framework using groovy

[Onboarding guide (this is mostly done using this archetype)](<add_documentation>)

[Usage Guide](<add_documentation>)

[Basic groovy syntax](<add_documentation>)


### Process tests

The process level tests are written in junit and sample tests are contained within the java test package.
The BPMN files under test should be added to the resources folder with all BPMNs loaded into an in-memory Fluxnova engine during test runtime

Some capabilities supported:
* Starting a process
* Mocking httpconnectors, callactivities and delegates
* Testing solutions (allowing call activities to be executed rather than mocked)
* Asserting process variable values


## Test Execution Tips

1. Make sure your project is built using JDK 21
2. If using IntellJ or some other IDE that requires marking directory sources then do this for the src, test and resources packages
2. Run 'mvn clean test' on first build and ensure all tests are running fine