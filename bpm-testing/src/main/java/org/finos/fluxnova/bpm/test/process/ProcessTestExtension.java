package org.finos.fluxnova.bpm.test.process;

import org.finos.fluxnova.bpm.test.TestException;
import org.finos.fluxnova.bpm.test.coverage.ProcessCoverage;
import org.finos.fluxnova.bpm.test.rules.MockConnectorRule;
import org.finos.fluxnova.bpm.test.scripting.ScriptTestUtils;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.finos.fluxnova.bpm.engine.repository.Deployment;
import org.finos.fluxnova.bpm.engine.repository.DeploymentBuilder;
import org.finos.fluxnova.bpm.engine.test.ProcessEngineRule;
import org.finos.fluxnova.bpm.engine.test.mock.Mocks;
import org.finos.fluxnova.bpm.model.bpmn.BpmnModelInstance;
import org.junit.ClassRule;
import org.junit.Rule;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

import static org.finos.fluxnova.bpm.engine.test.assertions.ProcessEngineTests.repositoryService;

public class ProcessTestExtension {

    @Rule
    public final ProcessEngineRule fluxnova = new ProcessEngineRule();

    @ClassRule
    public static final WireMockRule wireMockRule = new MockConnectorRule(8080);

    public static void setup(String bpmn, String... dependencies) {
        Class<?> caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        try {
            DeploymentBuilder deploymentBuilder = repositoryService().createDeployment();
            deployModel(bpmn, deploymentBuilder, caller);
            for (String dependency : dependencies) {
                deploy(dependency, deploymentBuilder);
            }
            deploymentBuilder.deploy();
            wireMockRule.start();
        } catch (Exception e) {
            throw new TestException("Error deploying resource", e);
        }
    }

    public static void teardown() {
        wireMockRule.stop();
        Mocks.reset();
        for (Deployment deployment : repositoryService().createDeploymentQuery().list()) {
            repositoryService().deleteDeployment(deployment.getId(), true);
        }
    }

    private static void deployModel(String fileName, DeploymentBuilder deployment, Class<?> caller) throws IOException {
        Resource bpmn = deploy(fileName, deployment);
        BpmnModelInstance bpmnModelInstance = ScriptTestUtils.getBpmnModelInstance(bpmn.getInputStream().readAllBytes());
        String processDefinitionKey = ScriptTestUtils.getProcessDefinitionKey(bpmnModelInstance);
        ProcessCoverage.register(processDefinitionKey, caller);
    }

    private static Resource deploy(String fileName, DeploymentBuilder deployment) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource("classpath:" + fileName);
        deployment.addInputStream(resource.getFilename(), resource.getInputStream());
        return resource;
    }
}
