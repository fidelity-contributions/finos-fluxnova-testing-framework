package org.finos.fluxnova.bpm.test.helpers;

import org.finos.fluxnova.bpm.test.TestException;
import org.finos.fluxnova.bpm.test.mockito.process.ProcessInstanceFake;
import org.finos.fluxnova.bpm.engine.ManagementService;
import org.finos.fluxnova.bpm.engine.impl.persistence.entity.EverLivingJobEntity;
import org.finos.fluxnova.bpm.engine.runtime.Job;
import org.finos.fluxnova.bpm.engine.runtime.JobQuery;
import org.finos.fluxnova.bpm.engine.test.assertions.ProcessEngineTests;
import org.finos.fluxnova.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;

import static org.finos.fluxnova.bpm.test.helpers.FlowHelpers.executeJob;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlowHelpersTest {

    @Mock
    ManagementService managementService;

    @Mock
    JobQuery jobQuery;

    @Mock
    Job job;

    @Test
    void testAdvanceFlowFully_shouldExecuteAllFlows() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::managementService).thenReturn(managementService);
            doReturn(jobQuery).when(managementService).createJobQuery();
            doReturn(jobQuery).when(jobQuery).processInstanceId(anyString());
            List<Job> firstJobs = getJobs("ActivityOne");
            List<Job> secondJobs = getJobs("ActivityTwo");
            List<Job> thirdJobs = getJobs("ActivityThree");
            when(jobQuery.list())
                    .thenReturn(firstJobs)
                    .thenReturn(secondJobs)
                    .thenReturn(thirdJobs)
                    .thenReturn(new ArrayList<>());
            FlowHelpers.advanceFlowUntilAction("SomeProcessInstance");
            verify(managementService, times(3)).executeJob("first");
            verify(managementService, times(3)).executeJob("second");
        }
    }

    @Test
    void testAdvanceFlowFully_shouldThrowException() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::managementService).thenReturn(managementService);
            doReturn(jobQuery).when(managementService).createJobQuery();
            doReturn(jobQuery).when(jobQuery).processInstanceId(anyString());
            List<Job> firstJobs = getJobs("ActivityOne");
            List<Job> secondJobs = getJobs("ActivityTwo");
            List<Job> thirdJobs = getJobs("ActivityThree");
            when(jobQuery.list())
                    .thenReturn(firstJobs)
                    .thenReturn(secondJobs)
                    .thenReturn(thirdJobs)
                    .thenReturn(new ArrayList<>());
            doThrow(TestException.class).when(managementService).executeJob(anyString());
            TestException exception =
                    assertThrows(TestException.class, () ->
                            FlowHelpers.advanceFlowUntilAction("SomeProcessInstance"));
            assertEquals("Error in executing async jobs", exception.getMessage());
        }
    }


    @Test
    void testAdvanceFlowFully_shouldExecuteNoFlowsWhenNoJobs() {
        try (MockedStatic<BpmnAwareTests> processEngineMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {
            processEngineMockedStatic.when(ProcessEngineTests::managementService).thenReturn(managementService);
            doReturn(jobQuery).when(managementService).createJobQuery();
            doReturn(jobQuery).when(jobQuery).processInstanceId(anyString());
            when(jobQuery.list()).thenReturn(new ArrayList<>());
            FlowHelpers.advanceFlowUntilAction("SomeProcessInstance");
            verify(managementService, times(0)).executeJob(anyString());
        }
    }

    @Test
    void testExecuteJob_shouldExecuteFlow() {
        try (MockedStatic<ProcessEngineTests> processEngineMockedStatic = Mockito.mockStatic(ProcessEngineTests.class);
             MockedStatic<BpmnAwareTests> bpmnAwareTestsMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)
        ) {
            executeJob(8, new ProcessInstanceFake.ProcessInstanceFakeBuilder().build());
            bpmnAwareTestsMockedStatic.verify(() -> BpmnAwareTests.execute(any()), times(8));
        }
    }

    @Test
    void testAdvanceParallelFlow_shouldExecuteAllFlows() {

        try (MockedStatic<ProcessEngineTests> processEngineTestsMockedStatic = Mockito.mockStatic(ProcessEngineTests.class);
             MockedStatic<BpmnAwareTests> bpmnAwareTestsMockedStatic = Mockito.mockStatic(BpmnAwareTests.class)) {

            processEngineTestsMockedStatic.when(ProcessEngineTests::managementService).thenReturn(managementService);
            when(managementService.createJobQuery()).thenReturn(jobQuery);
            when(jobQuery.processInstanceId(anyString())).thenReturn(jobQuery);

            Job first = buildJob("ActivityOne", "first");
            Job second = buildJob("ActivityOne", "second");
            Job third = buildJob("ActivityTwo", "third");

            when(jobQuery.list())
                    .thenReturn(List.of(first, second))
                    .thenReturn(List.of(third))
                    .thenReturn(List.of());

            FlowHelpers.advanceFlowThroughAllUntilAction("SomeProcessInstance");

            verify(managementService, times(1)).executeJob("first");
            verify(managementService, times(1)).executeJob("second");
            verify(managementService, times(1)).executeJob("third");

        }
    }

    @NotNull
    private List<Job> getJobs(String activityId) {
        List<Job> jobs = new ArrayList<>();
        Job firstJob = buildJob(activityId, "first");
        Job secondJob = buildJob(activityId, "second");
        jobs.add(firstJob);
        jobs.add(secondJob);
        return jobs;
    }

    private Job buildJob(String activityId, String id) {
        EverLivingJobEntity everLivingJobEntity = new EverLivingJobEntity();
        everLivingJobEntity.setActivityId(activityId);
        everLivingJobEntity.setId(id);
        return everLivingJobEntity;
    }
}
