package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.HistoryService;
import org.finos.fluxnova.bpm.engine.history.HistoricCaseActivityInstance;
import org.finos.fluxnova.bpm.engine.history.HistoricCaseActivityInstanceQuery;

public class HistoricCaseActivityInstanceQueryMock extends AbstractQueryMock<HistoricCaseActivityInstanceQueryMock, HistoricCaseActivityInstanceQuery, HistoricCaseActivityInstance, HistoryService> {

  public HistoricCaseActivityInstanceQueryMock() {
    super(HistoricCaseActivityInstanceQuery.class, HistoryService.class);
   }

}
