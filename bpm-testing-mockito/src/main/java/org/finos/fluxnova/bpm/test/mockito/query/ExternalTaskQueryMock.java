package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.ExternalTaskService;
import org.finos.fluxnova.bpm.engine.externaltask.ExternalTask;
import org.finos.fluxnova.bpm.engine.externaltask.ExternalTaskQuery;

public class ExternalTaskQueryMock extends AbstractQueryMock<ExternalTaskQueryMock, ExternalTaskQuery, ExternalTask, ExternalTaskService> {

  public ExternalTaskQueryMock() {
    super(ExternalTaskQuery.class, ExternalTaskService.class);
   }

}
