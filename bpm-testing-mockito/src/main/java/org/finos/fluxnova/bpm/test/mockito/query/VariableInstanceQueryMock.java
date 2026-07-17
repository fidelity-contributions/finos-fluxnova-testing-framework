package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.RuntimeService;
import org.finos.fluxnova.bpm.engine.runtime.VariableInstance;
import org.finos.fluxnova.bpm.engine.runtime.VariableInstanceQuery;

public class VariableInstanceQueryMock extends AbstractQueryMock<VariableInstanceQueryMock, VariableInstanceQuery, VariableInstance, RuntimeService> {

  public VariableInstanceQueryMock() {
    super(VariableInstanceQuery.class, RuntimeService.class);
   }

}
