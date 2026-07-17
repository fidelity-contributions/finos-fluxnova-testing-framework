package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.HistoryService;
import org.finos.fluxnova.bpm.engine.history.HistoricDetail;
import org.finos.fluxnova.bpm.engine.history.HistoricDetailQuery;

public class HistoricDetailQueryMock extends AbstractQueryMock<HistoricDetailQueryMock, HistoricDetailQuery, HistoricDetail, HistoryService> {

  public HistoricDetailQueryMock() {
    super(HistoricDetailQuery.class, HistoryService.class);
   }

}
