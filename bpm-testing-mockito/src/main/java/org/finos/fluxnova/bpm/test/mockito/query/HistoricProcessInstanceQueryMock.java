package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.HistoryService;
import org.finos.fluxnova.bpm.engine.history.HistoricProcessInstance;
import org.finos.fluxnova.bpm.engine.history.HistoricProcessInstanceQuery;

public class HistoricProcessInstanceQueryMock extends AbstractQueryMock<HistoricProcessInstanceQueryMock, HistoricProcessInstanceQuery, HistoricProcessInstance, HistoryService> {

  public HistoricProcessInstanceQueryMock() {
    super(HistoricProcessInstanceQuery.class, HistoryService.class);
   }

}
