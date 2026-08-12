package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.RuntimeService;
import org.finos.fluxnova.bpm.engine.runtime.Execution;
import org.finos.fluxnova.bpm.engine.runtime.ExecutionQuery;

public class ExecutionQueryMock extends AbstractQueryMock<ExecutionQueryMock, ExecutionQuery, Execution, RuntimeService> {

  public ExecutionQueryMock() {
    super(ExecutionQuery.class, RuntimeService.class);
   }

}
