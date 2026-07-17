package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.ManagementService;
import org.finos.fluxnova.bpm.engine.management.DeploymentStatistics;
import org.finos.fluxnova.bpm.engine.management.DeploymentStatisticsQuery;

public class DeploymentStatisticsQueryMock extends AbstractQueryMock<DeploymentStatisticsQueryMock, DeploymentStatisticsQuery, DeploymentStatistics, ManagementService> {

  public DeploymentStatisticsQueryMock() {
    super(DeploymentStatisticsQuery.class, ManagementService.class);
   }

}
