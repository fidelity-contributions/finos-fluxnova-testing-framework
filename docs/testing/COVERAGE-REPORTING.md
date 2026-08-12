# Introduction
The testing framework provides the capability of producing coverage reports on a BPMN and on an external script level. These reports consist of coverage metrics which are useful in determining how tested processes and scripts in a project are and if a certain threshold is met or not.

The framework gives the following capabilities from a coverage perspective:
- generates coverage reports
- ability to determine if a process or an external script falls below a specified coverage threshold. By default, the coverage threshold is set to 80%
- fails the build if coverage is not met. This is particularly useful as part of CI for the build to fail during packaging if coverage isn't met
- coverage reports per process / external script are bundled as part of the deployment to camunda

Note:
Tests must be run first for the coverage plugin to compute the coverages.
The coverage plugin is only invoked during a maven run in the verify phase. This means that all maven runs must include the verify phase.
If using mvn clean test this must be updated to mvn clean test verify OR mvn clean install can be used. Either of these will first run the tests and then invoke the coverage plugin.

### Coverage Integration
The coverage is invoked through a maven plugin and is required to be included in the testing project's pom.
```xml
      <plugin>
        <groupId>org.finos.fluxnova.bpm.test</groupId>
        <artifactId>bpm-coverage-collection-plugin</artifactId>
        <version>${bpm-testing.version}</version>
        <executions>
          <execution>
            <goals>
              <goal>collect-coverage</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
```

This plugin collects insights computed during test executions and uses these insights to produce a coverage report for all bpmns and external scripts in the testing project.

### Coverage Reporting
Each BPMN and external script in the testing project will have an individual coverage report generated.
These reports are written to the build directory in the project with the full path being "target/coverage-collection/code-coverage". The reports with the json extension are the reports of interest.

The naming convention of these files combine the name of the bpmn / external script with the .coverage.json extension appended.

Examples:
A BPMN file named my_model.bpmn will have a corresponding coverage file called my_model.bpmn.coverage.json.
An external script called my_external_script.groovy will have a corresponding coverage file called my_external_script.groovy.coverage.json

#### BPMN Coverage Report File
An individual report file is generated for each BPMN file in the project.
This report file consists of the following properties:
- metadata - this contains metadata regarding the bpmn
    - name - the name of the bpmn model
    - processDefinitionKey - the process definition key assigned to the model
    - threshold - the coverage threshold applied during coverage checks
- processCoverage - this is the coverage percentage obtained from all running process level tests for this model
- scriptCoverage - this is the total script coverage across all inline scripts in this model obtained from all running inline script tests for this model
- scripts - this is a list of all script tasks with inline scripts in this model with individual coverage evaluation
    - activityid - this is the activity id for a script task with an inline script in this model
    - coverage - this is the coverage for the inline script obtainted from all running scripts tests for that script
- pass - this is a boolean value is evaluated with the following conditions
    - true - if both the processCoverage and scriptCoverage are equal or above the coverage threshold value
    - false - if either the processCoverage or the scriptCoverage are below the coverage threshold value
      
Example:
```json
{
  "metadata": {
    "name": "my_model.bpmn",
    "processDefinitionKey": "someProcessDefinitionKey",
    "threshold": 80.0
  },
  "processCoverage": 98.96,
  "scriptCoverage": 100.00,
  "scripts": [
    {
      "activityId": "Activity_05jj074",
      "coverage": 100.0
    },
    {
      "activityId": "Activity_193daws",
      "coverage": 100.0
    }
  ],
  "pass": true
}
```

#### External Script Coverage Report File
An individual report file is generated for each external script file in the project.
This report file consists of the following properties:
- metadata - this contains metadata regarding the external script
    - name - the name of the external script file
    - threshold - the coverage threshold applied during coverage checks
- coverage - this is the computed coverage across all tests for this external script
- pass - this boolean value is evaluated to true if the computed coverage is equal to or above the coverage threshold value
Example:
```json
{
  "metadata": {
    "name": "some_external_script.js",
    "threshold": 80.0
  },
  "coverage": 80.95,
  "pass": true
}
```

#### Configurable Properties
The different behaviours performed by the coverage plugin are configurable using maven arguments as properties.
Example of use: mvn clean test verify -DnameOfConfigurableProperty=\<value>

- coverageThreshold - Default 80%. This accepts a whole number or decimal number as value. The plugin will perform its coverage checks against this value. Example: mvn clean test verify -DcoverageThreshold=90
- ignoreCoverageFailure - Default false. This accepts a boolean value. If set to false the build will fail if coverage checks are not met; if set to true the build will still pass. Example: mvn clean test verify -DignoreCoverageFailure=true
- skipCoverage - Default false. This accepts a boolean value. If set to true then coverage collection is skipped; if set to false coverage collection runs. Example: mvn clean test verify -DskipCoverage=true 

#### Maven Output
The maven output will show which BPMNs and/or external scripts did not meet the coverage threshold. Depending on the configuration the build will fail on coverages not being met.

When coverage is enabled maven will output a new warning log for each missed coverage in a BPMN and/or external script and it's type where type:
- PROCESS means coverage is low in that BPMNs process tests
- INLINE_SCRIPT means total coverage is low in that BPMNs inline scripts tests
- EXTERNAL_SCRIPT means coverage is low in that external scripts tests

Note:
You need to ensure the acron library is installed for coverage reports to run.
Run as follows (before running the maven build):
npm install   

### Sonar Coverage
The coverage plugin also supports viewing coverage metrics and static code analysis issues for both external and inline scripts.
The plugin provides the following to allow for sonar integration:
1. Extracts all inline scripts from all models found in the project and isolates them into individual script files.
   These files can be found in sonar/generated/scripts directory after plugin has finished executing.
2. Generates a Sonar-compatible coverage report which contains coverage metrics for the isolated inline script files and existing external scripts in the project.

Below is one example in how sonar can be used to run tests and upload report and source to SonarQube

mvn clean install sonar:sonar  \
-Dsonar.projectKey=<project_key> \
-Dsonar.projectName='<project_name>' \
-Dsonar.token=<sonar_token> \
-Dsonar.branch.name="<optional_branch_name>"

To successfully start a scan and see results in SonarQube you must have a project created. This project will contain a project key and project name which must be passed in to sonar.
A SonarQube token is also required to authenticate to the SonarQube instance.
It is important to ensure that external scripts are also included in sonar sources. To modify sources update the sonar.source property in the pom.xml in the testing project to include paths to all external scripts. Inline scripts are included by default.

Example:
<sonar.sources>sonar/generated/scripts,bpmn/scripts,other/scripts</sonar.sources>
To allow Sonar to analyze files outside your testing project/module, Maven must understand the repository as a multi-module build.
Without a root parent POM:
- Sonar analysis is anchored to the module directory
- Paths outside the module are unreliable. This is a Maven constraint not a bug within Sonar.

### Supported Repository Layouts
#### Simple Layout (No External Sources)

Use this when everything requiring a scan lives inside the testing project.
Run Sonar from inside the project.
No root POM required.

#### Multi-Module Layout
Use this when anything requiring a scan lives outside the testing module.
For this the project must be a multi-module Maven project:
1. Add a root pom packaged as a pom
2. Modify the testing module to inherit the root POM.
3. Move all sonar.* properties from testing module to root POM
4. Update the bpm-coverage-collection-plugin to pass in srcDirectory property pointing to root:
```xml
     <plugin>
        <groupId>org.finos.fluxnova.bpm.test</groupId>
        <artifactId>bpm-coverage-collection-plugin</artifactId>
        <version>${bpm-testing.version}</version>
        <configuration>
          <srcDirectory>..</srcDirectory>
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>collect-coverage</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
```
5. To execute:
- Run tests as normal
- Then execute sonar from root:

mvn -f \<module>/pom.xml sonar:sonar  \
-Dsonar.projectKey=<project_key> \
-Dsonar.projectName='<project_name>' \
-Dsonar.token=<sonar_token> \
-Dsonar.branch.name="<optional_branch_name>"

