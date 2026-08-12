package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.HistoryService;
import org.finos.fluxnova.bpm.engine.history.HistoricJobLog;
import org.finos.fluxnova.bpm.engine.history.HistoricJobLogQuery;

public class HistoricJobLogQueryMock extends AbstractQueryMock<HistoricJobLogQueryMock, HistoricJobLogQuery, HistoricJobLog, HistoryService> {

  public HistoricJobLogQueryMock() {
    super(HistoricJobLogQuery.class, HistoryService.class);
   }

}
