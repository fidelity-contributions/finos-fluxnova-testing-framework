package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.ManagementService;
import org.finos.fluxnova.bpm.engine.runtime.Job;
import org.finos.fluxnova.bpm.engine.runtime.JobQuery;

public class JobQueryMock extends AbstractQueryMock<JobQueryMock, JobQuery, Job, ManagementService> {

  public JobQueryMock() {
    super(JobQuery.class, ManagementService.class);
   }

}
