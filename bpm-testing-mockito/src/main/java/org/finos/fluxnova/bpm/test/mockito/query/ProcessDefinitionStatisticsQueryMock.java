package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.ManagementService;
import org.finos.fluxnova.bpm.engine.management.ProcessDefinitionStatistics;
import org.finos.fluxnova.bpm.engine.management.ProcessDefinitionStatisticsQuery;

public class ProcessDefinitionStatisticsQueryMock extends AbstractQueryMock<ProcessDefinitionStatisticsQueryMock, ProcessDefinitionStatisticsQuery, ProcessDefinitionStatistics, ManagementService> {

  public ProcessDefinitionStatisticsQueryMock() {
    super(ProcessDefinitionStatisticsQuery.class, ManagementService.class);
   }

}
