package uk.co.mruoc.nac.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.co.mruoc.nac.usecases.JwtTokenSupplier;
import uk.co.mruoc.nac.usecases.UserFinder;

@RequiredArgsConstructor
public class SpringJwtTokenSupplier implements JwtTokenSupplier {

  private final SpringAuthenticationConverter converter;

  public SpringJwtTokenSupplier(UserFinder finder) {
    this(new SpringAuthenticationConverter(finder));
  }

  @Override
  public String get() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return converter.toJwtTokenValue(auth);
  }
}
