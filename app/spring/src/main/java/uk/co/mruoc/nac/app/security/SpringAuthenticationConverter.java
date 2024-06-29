package uk.co.mruoc.nac.app.security;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.usecases.UserFinder;
import uk.co.mruoc.nac.usecases.UserNotAuthenticatedException;

@RequiredArgsConstructor
public class SpringAuthenticationConverter {

  private final UserFinder userFinder;

  public User toUser(Authentication auth) {
    if (auth instanceof JwtAuthenticationToken jwtAuth) {
      return toUser(jwtAuth);
    }
    throw new UserNotAuthenticatedException();
  }

  private User toUser(JwtAuthenticationToken jwtAuth) {
    Map<String, Object> attributes = jwtAuth.getTokenAttributes();
    String username = attributes.get("username").toString();
    return userFinder.forceGetByUsername(username);
  }
}
