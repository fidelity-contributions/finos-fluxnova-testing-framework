package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.CaseService;
import org.finos.fluxnova.bpm.engine.runtime.CaseInstance;
import org.finos.fluxnova.bpm.engine.runtime.CaseInstanceQuery;

public class CaseInstanceQueryMock extends AbstractQueryMock<CaseInstanceQueryMock, CaseInstanceQuery, CaseInstance, CaseService> {

  public CaseInstanceQueryMock() {
    super(CaseInstanceQuery.class, CaseService.class);
   }

}
