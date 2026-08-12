package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.IdentityService;
import org.finos.fluxnova.bpm.engine.identity.Tenant;
import org.finos.fluxnova.bpm.engine.identity.TenantQuery;

public class TenantQueryMock extends AbstractQueryMock<TenantQueryMock, TenantQuery, Tenant, IdentityService> {

  public TenantQueryMock() {
    super(TenantQuery.class, IdentityService.class);
   }

}
