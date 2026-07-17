package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.HistoryService;
import org.finos.fluxnova.bpm.engine.history.HistoricTaskInstance;
import org.finos.fluxnova.bpm.engine.history.HistoricTaskInstanceQuery;

public class HistoricTaskInstanceQueryMock extends AbstractQueryMock<HistoricTaskInstanceQueryMock, HistoricTaskInstanceQuery, HistoricTaskInstance, HistoryService> {

  public HistoricTaskInstanceQueryMock() {
    super(HistoricTaskInstanceQuery.class, HistoryService.class);
   }

}
