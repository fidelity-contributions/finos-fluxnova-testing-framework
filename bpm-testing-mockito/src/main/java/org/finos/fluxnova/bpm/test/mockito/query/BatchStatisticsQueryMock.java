package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.ManagementService;
import org.finos.fluxnova.bpm.engine.batch.BatchStatistics;
import org.finos.fluxnova.bpm.engine.batch.BatchStatisticsQuery;

public class BatchStatisticsQueryMock extends AbstractQueryMock<BatchStatisticsQueryMock, BatchStatisticsQuery, BatchStatistics, ManagementService> {

  public BatchStatisticsQueryMock() {
    super(BatchStatisticsQuery.class, ManagementService.class);
   }

}
