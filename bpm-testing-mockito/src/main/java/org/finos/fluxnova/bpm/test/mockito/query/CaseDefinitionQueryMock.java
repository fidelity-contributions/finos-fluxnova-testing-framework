package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.RepositoryService;
import org.finos.fluxnova.bpm.engine.repository.CaseDefinition;
import org.finos.fluxnova.bpm.engine.repository.CaseDefinitionQuery;

public class CaseDefinitionQueryMock extends AbstractQueryMock<CaseDefinitionQueryMock, CaseDefinitionQuery, CaseDefinition, RepositoryService> {

  public CaseDefinitionQueryMock() {
    super(CaseDefinitionQuery.class, RepositoryService.class);
   }

}
