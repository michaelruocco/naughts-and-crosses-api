package uk.co.mruoc.nac.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.usecases.AuthenticatedUserSupplier;
import uk.co.mruoc.nac.usecases.UserFinder;

@RequiredArgsConstructor
public class SpringAuthenticatedUserSupplier implements AuthenticatedUserSupplier {

  private final SpringAuthenticationConverter converter;

  public SpringAuthenticatedUserSupplier(UserFinder finder) {
    this(new SpringAuthenticationConverter(finder));
  }

  @Override
  public User get() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return converter.toUser(auth);
  }
}
