package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.RuntimeService;
import org.finos.fluxnova.bpm.engine.runtime.ProcessInstance;
import org.finos.fluxnova.bpm.engine.runtime.ProcessInstanceQuery;

public class ProcessInstanceQueryMock extends AbstractQueryMock<ProcessInstanceQueryMock, ProcessInstanceQuery, ProcessInstance, RuntimeService> {

  public ProcessInstanceQueryMock() {
    super(ProcessInstanceQuery.class, RuntimeService.class);
   }

}
