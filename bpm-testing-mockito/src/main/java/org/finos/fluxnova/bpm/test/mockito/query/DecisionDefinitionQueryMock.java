package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.RepositoryService;
import org.finos.fluxnova.bpm.engine.repository.DecisionDefinition;
import org.finos.fluxnova.bpm.engine.repository.DecisionDefinitionQuery;

public class DecisionDefinitionQueryMock extends AbstractQueryMock<DecisionDefinitionQueryMock, DecisionDefinitionQuery, DecisionDefinition, RepositoryService> {

  public DecisionDefinitionQueryMock() {
    super(DecisionDefinitionQuery.class, RepositoryService.class);
   }

}
