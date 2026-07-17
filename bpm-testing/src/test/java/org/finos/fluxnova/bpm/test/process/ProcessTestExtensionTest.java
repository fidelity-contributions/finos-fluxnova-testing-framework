package org.finos.fluxnova.bpm.test.process;

import org.finos.fluxnova.bpm.test.TestException;
import org.finos.fluxnova.bpm.test.coverage.ProcessCoverage;
import org.finos.fluxnova.bpm.engine.RepositoryService;
import org.finos.fluxnova.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.finos.fluxnova.bpm.engine.repository.Deployment;
import org.finos.fluxnova.bpm.engine.repository.DeploymentBuilder;
import org.finos.fluxnova.bpm.engine.repository.DeploymentQuery;
import org.finos.fluxnova.bpm.engine.test.assertions.ProcessEngineTests;
import org.finos.fluxnova.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessTestExtensionTest {

    @Mock
    private RepositoryService repositoryService;

    @Mock
    private DeploymentQuery deploymentQuery;

    @Mock
    private DeploymentBuilder deploymentBuilder;

    @Mock
    private Deployment deployment;

    @Test
    void teardown_shouldRedeployModels() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::repositoryService).thenReturn(repositoryService);
            doReturn(deploymentQuery).when(repositoryService).createDeploymentQuery();
            List<Deployment> deployments = buildDeployments();
            doReturn(deployments).when(deploymentQuery).list();
            ProcessTestExtension.teardown();
            verify(repositoryService, times(1)).deleteDeployment("1", true);
            verify(repositoryService, times(1)).deleteDeployment("2", true);
        }
    }

    @Test
    void setup_shouldDeployModel() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class);
             MockedStatic<ProcessCoverage> processCoverageMockedStatic = Mockito.mockStatic(ProcessCoverage.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::repositoryService).thenReturn(repositoryService);
            doReturn(deploymentBuilder).when(repositoryService).createDeployment();
            doReturn(deploymentBuilder).when(deploymentBuilder).addInputStream(any(), any());
            doReturn(deployment).when(deploymentBuilder).deploy();
            ProcessTestExtension.setup("unit/script-task.bpmn");
            processCoverageMockedStatic.verify(() -> ProcessCoverage.register("Process_1k7woqc", ProcessTestExtensionTest.class), times(1));
            verify(deploymentBuilder, times(1)).addInputStream(eq("script-task.bpmn"), any());
        }
    }

    @Test
    void setup_shouldDeployModelAndDependencies() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class);
             MockedStatic<ProcessCoverage> processCoverageMockedStatic = Mockito.mockStatic(ProcessCoverage.class)) {
            processEngineMockedStatic.when(BpmnAwareTests::repositoryService).thenReturn(repositoryService);
            doReturn(deploymentBuilder).when(repositoryService).createDeployment();
            doReturn(deploymentBuilder).when(deploymentBuilder).addInputStream(any(), any());
            doReturn(deployment).when(deploymentBuilder).deploy();
            ProcessTestExtension.setup("unit/script-task.bpmn", "unit/hello-world.js", "unit/hello-world.groovy");
            processCoverageMockedStatic.verify(() -> ProcessCoverage.register("Process_1k7woqc", ProcessTestExtensionTest.class), times(1));
            verify(deploymentBuilder, times(1)).addInputStream(eq("script-task.bpmn"), any());
            verify(deploymentBuilder, times(1)).addInputStream(eq("hello-world.groovy"), any());
            verify(deploymentBuilder, times(1)).addInputStream(eq("hello-world.js"), any());

        }
    }

    @Test
    void setup_throwsDAPTestExceptionOnError() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::repositoryService).thenReturn(repositoryService);
            doReturn(deploymentBuilder).when(repositoryService).createDeployment();
            doReturn(deploymentBuilder).when(deploymentBuilder).addInputStream(any(), any());
            TestException exception = assertThrows(TestException.class, () -> {
                ProcessTestExtension.setup("unit/hello-world.js");
            });
            assertEquals("Error deploying resource", exception.getMessage());

        }
    }

    private List<Deployment> buildDeployments() {
        List<Deployment> deployments = new ArrayList<>();
        DeploymentEntity firstDeployment = new DeploymentEntity();
        firstDeployment.setId("1");
        deployments.add(firstDeployment);
        DeploymentEntity secondDeployment = new DeploymentEntity();
        secondDeployment.setId("2");
        deployments.add(secondDeployment);
        return deployments;
    }
}
