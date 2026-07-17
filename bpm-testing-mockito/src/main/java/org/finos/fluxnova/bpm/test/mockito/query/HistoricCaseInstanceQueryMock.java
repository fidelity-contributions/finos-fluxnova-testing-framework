package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.HistoryService;
import org.finos.fluxnova.bpm.engine.history.HistoricCaseInstance;
import org.finos.fluxnova.bpm.engine.history.HistoricCaseInstanceQuery;

public class HistoricCaseInstanceQueryMock extends AbstractQueryMock<HistoricCaseInstanceQueryMock, HistoricCaseInstanceQuery, HistoricCaseInstance, HistoryService> {

  public HistoricCaseInstanceQueryMock() {
    super(HistoricCaseInstanceQuery.class, HistoryService.class);
   }

}
