package org.finos.fluxnova.bpm.test.helpers;

import org.finos.fluxnova.bpm.test.TestException;
import org.finos.fluxnova.bpm.engine.runtime.Job;
import org.finos.fluxnova.bpm.engine.runtime.ProcessInstance;

import java.util.List;

import static java.lang.Thread.sleep;
import static org.finos.fluxnova.bpm.engine.test.assertions.ProcessEngineTests.managementService;
import static org.finos.fluxnova.bpm.engine.test.assertions.bpmn.BpmnAwareTests.execute;
import static org.finos.fluxnova.bpm.engine.test.assertions.bpmn.BpmnAwareTests.job;

public class FlowHelpers {

    private FlowHelpers() {}

    public static void advanceFlowUntilAction(String processInstanceId) {
        while (true) {
            try {
                List<Job> currentJobList = managementService().createJobQuery().processInstanceId(processInstanceId).list();
                if (currentJobList.isEmpty()) {
                    break;
                }
                for (Job currentJob : currentJobList) {
                    managementService().executeJob(currentJob.getId());
                    sleep(500);
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                throw new TestException("Error in executing async jobs", e);
            }
        }
    }

    public static void executeJob(int executionTimes, ProcessInstance instance)
    {
        for(int i = 0; i < executionTimes; i++)
        {
            execute(job(instance));
        }
    }

    public static void advanceFlowThroughAllUntilAction(String processInstanceId) {
        //This method handles parallel jobs as well, it will keep executing until there are no more jobs left in the system
        List<Job> jobs = managementService().createJobQuery().processInstanceId(processInstanceId).list();

        while (!jobs.isEmpty())
        {
            jobs.forEach(job -> managementService().executeJob(job.getId()));
            jobs = managementService().createJobQuery().processInstanceId(processInstanceId).list();
        }
    }
}
