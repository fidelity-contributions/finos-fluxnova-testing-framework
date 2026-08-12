package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.RuntimeService;
import org.finos.fluxnova.bpm.engine.runtime.Incident;
import org.finos.fluxnova.bpm.engine.runtime.IncidentQuery;

public class IncidentQueryMock extends AbstractQueryMock<IncidentQueryMock, IncidentQuery, Incident, RuntimeService> {

  public IncidentQueryMock() {
    super(IncidentQuery.class, RuntimeService.class);
   }

}
