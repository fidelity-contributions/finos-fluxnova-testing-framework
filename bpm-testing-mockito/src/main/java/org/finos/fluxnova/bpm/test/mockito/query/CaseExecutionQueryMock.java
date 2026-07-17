package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.CaseService;
import org.finos.fluxnova.bpm.engine.runtime.CaseExecution;
import org.finos.fluxnova.bpm.engine.runtime.CaseExecutionQuery;

public class CaseExecutionQueryMock extends AbstractQueryMock<CaseExecutionQueryMock, CaseExecutionQuery, CaseExecution, CaseService> {

  public CaseExecutionQueryMock() {
    super(CaseExecutionQuery.class, CaseService.class);
   }

}
