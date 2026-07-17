package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.HistoryService;
import org.finos.fluxnova.bpm.engine.history.HistoricVariableInstance;
import org.finos.fluxnova.bpm.engine.history.HistoricVariableInstanceQuery;

public class HistoricVariableInstanceQueryMock extends AbstractQueryMock<HistoricVariableInstanceQueryMock, HistoricVariableInstanceQuery, HistoricVariableInstance, HistoryService> {

  public HistoricVariableInstanceQueryMock() {
    super(HistoricVariableInstanceQuery.class, HistoryService.class);
   }

}
