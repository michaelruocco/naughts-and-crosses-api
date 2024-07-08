package uk.co.mruoc.nac.user.inmemory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.usecases.TokenExpiredException;

@RequiredArgsConstructor
public class JwtValidator {

  private final Clock clock;
  private final ObjectMapper mapper;
  private final Base64.Decoder decoder;

  public JwtValidator(Clock clock, ObjectMapper mapper) {
    this(clock, mapper, Base64.getDecoder());
  }

  public void validate(String jwt) {
    if (isExpired(jwt)) {
      throw new TokenExpiredException(jwt);
    }
  }

  private boolean isExpired(String jwt) {
    Instant expiry = toExpiry(jwt);
    return expiry.isBefore(clock.instant());
  }

  private Instant toExpiry(String jwt) {
    String body = extractBody(jwt);
    return bodyToExpiry(body);
  }

  private String extractBody(String jwt) {
    String[] parts = jwt.split("\\.");
    if (parts.length < 2) {
      throw new InvalidJwtException(jwt);
    }
    return new String(decoder.decode(parts[1]));
  }

  private Instant bodyToExpiry(String body) {
    try {
      JsonNode node = mapper.readTree(body);
      return toExpiry(node);
    } catch (JsonProcessingException e) {
      throw new InvalidJwtException(e);
    }
  }

  private Instant toExpiry(JsonNode body) {
    JsonNode expiry = body.get("exp");
    if (expiry.isNull()) {
      throw new InvalidJwtException(String.format("expiry not found in body %s", body));
    }
    return Instant.ofEpochSecond(expiry.asInt());
  }
}
