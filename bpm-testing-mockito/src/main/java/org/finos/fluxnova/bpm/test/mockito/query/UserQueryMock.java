package org.finos.fluxnova.bpm.test.mockito.query;

import org.finos.fluxnova.bpm.engine.IdentityService;
import org.finos.fluxnova.bpm.engine.identity.User;
import org.finos.fluxnova.bpm.engine.identity.UserQuery;

public class UserQueryMock extends AbstractQueryMock<UserQueryMock, UserQuery, User, IdentityService> {

  public UserQueryMock() {
    super(UserQuery.class, IdentityService.class);
   }

}
