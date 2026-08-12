package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.ManagementService;
import org.finos.fluxnova.bpm.engine.batch.Batch;
import org.finos.fluxnova.bpm.engine.batch.BatchQuery;

public class BatchQueryMock extends AbstractQueryMock<BatchQueryMock, BatchQuery, Batch, ManagementService> {

  public BatchQueryMock() {
    super(BatchQuery.class, ManagementService.class);
   }

}
