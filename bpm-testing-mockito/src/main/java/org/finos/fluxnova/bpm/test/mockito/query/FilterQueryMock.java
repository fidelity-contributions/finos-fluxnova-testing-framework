package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.FilterService;
import org.finos.fluxnova.bpm.engine.filter.Filter;
import org.finos.fluxnova.bpm.engine.filter.FilterQuery;

public class FilterQueryMock extends AbstractQueryMock<FilterQueryMock, FilterQuery, Filter, FilterService> {

  public FilterQueryMock() {
    super(FilterQuery.class, FilterService.class);
   }

}
