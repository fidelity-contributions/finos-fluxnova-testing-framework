package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.RuntimeService;
import org.finos.fluxnova.bpm.engine.runtime.EventSubscription;
import org.finos.fluxnova.bpm.engine.runtime.EventSubscriptionQuery;

public class EventSubscriptionQueryMock extends AbstractQueryMock<EventSubscriptionQueryMock, EventSubscriptionQuery, EventSubscription, RuntimeService> {

  public EventSubscriptionQueryMock() {
    super(EventSubscriptionQuery.class, RuntimeService.class);
   }

}
