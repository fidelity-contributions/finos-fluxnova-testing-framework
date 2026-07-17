package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.HistoryService;
import org.finos.fluxnova.bpm.engine.history.HistoricIncident;
import org.finos.fluxnova.bpm.engine.history.HistoricIncidentQuery;

public class HistoricIncidentQueryMock extends AbstractQueryMock<HistoricIncidentQueryMock, HistoricIncidentQuery, HistoricIncident, HistoryService> {

  public HistoricIncidentQueryMock() {
    super(HistoricIncidentQuery.class, HistoryService.class);
   }

}
