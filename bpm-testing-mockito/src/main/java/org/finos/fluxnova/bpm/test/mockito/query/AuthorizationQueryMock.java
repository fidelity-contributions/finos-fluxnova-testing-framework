package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.AuthorizationService;
import org.finos.fluxnova.bpm.engine.authorization.Authorization;
import org.finos.fluxnova.bpm.engine.authorization.AuthorizationQuery;

public class AuthorizationQueryMock extends AbstractQueryMock<AuthorizationQueryMock, AuthorizationQuery, Authorization, AuthorizationService> {

  public AuthorizationQueryMock() {
    super(AuthorizationQuery.class, AuthorizationService.class);
   }

}
