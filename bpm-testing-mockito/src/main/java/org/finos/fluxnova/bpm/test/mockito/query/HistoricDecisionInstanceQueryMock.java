package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.HistoryService;
import org.finos.fluxnova.bpm.engine.history.HistoricDecisionInstance;
import org.finos.fluxnova.bpm.engine.history.HistoricDecisionInstanceQuery;

public class HistoricDecisionInstanceQueryMock extends AbstractQueryMock<HistoricDecisionInstanceQueryMock, HistoricDecisionInstanceQuery, HistoricDecisionInstance, HistoryService> {

  public HistoricDecisionInstanceQueryMock() {
    super(HistoricDecisionInstanceQuery.class, HistoryService.class);
   }

}
