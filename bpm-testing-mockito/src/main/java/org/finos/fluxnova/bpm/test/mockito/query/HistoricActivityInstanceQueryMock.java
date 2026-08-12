package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.HistoryService;
import org.finos.fluxnova.bpm.engine.history.HistoricActivityInstance;
import org.finos.fluxnova.bpm.engine.history.HistoricActivityInstanceQuery;

public class HistoricActivityInstanceQueryMock extends AbstractQueryMock<HistoricActivityInstanceQueryMock, HistoricActivityInstanceQuery, HistoricActivityInstance, HistoryService> {

  public HistoricActivityInstanceQueryMock() {
    super(HistoricActivityInstanceQuery.class, HistoryService.class);
   }

}
