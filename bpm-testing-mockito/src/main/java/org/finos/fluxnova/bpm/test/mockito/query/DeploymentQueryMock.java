package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.RepositoryService;
import org.finos.fluxnova.bpm.engine.repository.Deployment;
import org.finos.fluxnova.bpm.engine.repository.DeploymentQuery;

public class DeploymentQueryMock extends AbstractQueryMock<DeploymentQueryMock, DeploymentQuery, Deployment, RepositoryService> {

  public DeploymentQueryMock() {
    super(DeploymentQuery.class, RepositoryService.class);
   }

}
