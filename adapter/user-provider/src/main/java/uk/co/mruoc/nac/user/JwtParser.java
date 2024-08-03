package uk.co.mruoc.nac.user;

import java.time.Instant;

public interface JwtParser {
  String toUsername(String token);

  Instant toExpiry(String token);
}
