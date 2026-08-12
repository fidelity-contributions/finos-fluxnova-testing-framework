package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.HistoryService;
import org.finos.fluxnova.bpm.engine.history.HistoricIdentityLinkLog;
import org.finos.fluxnova.bpm.engine.history.HistoricIdentityLinkLogQuery;

public class HistoricIdentityLinkLogQueryMock extends AbstractQueryMock<HistoricIdentityLinkLogQueryMock, HistoricIdentityLinkLogQuery, HistoricIdentityLinkLog, HistoryService> {

  public HistoricIdentityLinkLogQueryMock() {
    super(HistoricIdentityLinkLogQuery.class, HistoryService.class);
   }

}
