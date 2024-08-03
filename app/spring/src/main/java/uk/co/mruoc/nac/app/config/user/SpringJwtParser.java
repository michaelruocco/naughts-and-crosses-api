package uk.co.mruoc.nac.app.config.user;

import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import uk.co.mruoc.nac.user.JwtParser;
import uk.co.mruoc.nac.user.inmemory.InvalidJwtException;

@RequiredArgsConstructor
public class SpringJwtParser implements JwtParser {

  private final JwtDecoder jwtDecoder;

  @Override
  public String toUsername(String token) {
    Jwt jwt = jwtDecoder.decode(token);
    return Optional.ofNullable(jwt.getClaims().get("username"))
        .map(Object::toString)
        .orElseThrow(() -> new InvalidJwtException("username claim not found"));
  }

  @Override
  public Instant toExpiry(String token) {
    Jwt jwt = jwtDecoder.decode(token);
    return jwt.getExpiresAt();
  }
}
