package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.HistoryService;
import org.finos.fluxnova.bpm.engine.batch.history.HistoricBatch;
import org.finos.fluxnova.bpm.engine.batch.history.HistoricBatchQuery;

public class HistoricBatchQueryMock extends AbstractQueryMock<HistoricBatchQueryMock, HistoricBatchQuery, HistoricBatch, HistoryService> {

  public HistoricBatchQueryMock() {
    super(HistoricBatchQuery.class, HistoryService.class);
   }

}
