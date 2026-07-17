package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.HistoryService;
import org.finos.fluxnova.bpm.engine.history.HistoricActivityStatistics;
import org.finos.fluxnova.bpm.engine.history.HistoricActivityStatisticsQuery;

public class HistoricActivityStatisticsQueryMock extends AbstractQueryMock<HistoricActivityStatisticsQueryMock, HistoricActivityStatisticsQuery, HistoricActivityStatistics, HistoryService> {

  public HistoricActivityStatisticsQueryMock() {
    super(HistoricActivityStatisticsQuery.class, HistoryService.class);
   }

}
