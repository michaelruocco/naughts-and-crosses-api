package uk.co.mruoc.nac.user.inmemory;

import java.time.Clock;
import java.time.Instant;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.usecases.JwtExpiredException;
import uk.co.mruoc.nac.user.JwtParser;

@Builder
@RequiredArgsConstructor
public class JwtValidator {

  private final Clock clock;
  private final JwtParser parser;

  public void validate(String jwt) {
    if (isExpired(jwt)) {
      throw new JwtExpiredException(jwt);
    }
  }

  private boolean isExpired(String jwt) {
    Instant expiry = parser.toExpiry(jwt);
    Instant now = clock.instant();
    return now.isAfter(expiry);
  }
}
