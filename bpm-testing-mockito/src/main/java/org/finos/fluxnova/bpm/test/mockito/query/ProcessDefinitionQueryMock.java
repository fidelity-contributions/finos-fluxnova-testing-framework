package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.RepositoryService;
import org.finos.fluxnova.bpm.engine.repository.ProcessDefinition;
import org.finos.fluxnova.bpm.engine.repository.ProcessDefinitionQuery;

public class ProcessDefinitionQueryMock extends AbstractQueryMock<ProcessDefinitionQueryMock, ProcessDefinitionQuery, ProcessDefinition, RepositoryService> {

  public ProcessDefinitionQueryMock() {
    super(ProcessDefinitionQuery.class, RepositoryService.class);
   }

}
