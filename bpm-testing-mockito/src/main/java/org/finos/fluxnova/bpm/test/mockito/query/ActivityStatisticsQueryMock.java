package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.ManagementService;
import org.finos.fluxnova.bpm.engine.management.ActivityStatistics;
import org.finos.fluxnova.bpm.engine.management.ActivityStatisticsQuery;

public class ActivityStatisticsQueryMock extends AbstractQueryMock<ActivityStatisticsQueryMock, ActivityStatisticsQuery, ActivityStatistics, ManagementService> {

  public ActivityStatisticsQueryMock() {
    super(ActivityStatisticsQuery.class, ManagementService.class);
   }

}
