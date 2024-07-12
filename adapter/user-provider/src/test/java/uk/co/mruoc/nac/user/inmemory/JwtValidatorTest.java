package uk.co.mruoc.nac.user.inmemory;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Clock;
import java.time.Instant;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.usecases.JwtExpiredException;

class JwtValidatorTest {

  private static final Instant NOW = Instant.parse("2024-07-12T20:25:01Z");

  private final Clock clock = Clock.fixed(NOW, UTC);
  private final ObjectMapper mapper = new ObjectMapper();

  private final JwtValidator validator = new JwtValidator(clock, mapper);

  @Test
  void shouldThrowExceptionIfJwtIsInvalid() {
    String jwt = "invalid-jwt";

    Throwable error = catchThrowable(() -> validator.validate(jwt));

    assertThat(error).isInstanceOf(InvalidJwtException.class).hasMessage(jwt);
  }

  @Test
  void shouldThrowExceptionIfJwtExpiryIsNull() {
    String jwt = jwtWithExpiry(null);

    Throwable error = catchThrowable(() -> validator.validate(jwt));

    assertThat(error)
        .isInstanceOf(InvalidJwtException.class)
        .hasMessage(
            "expiry not found in body {\"sub\":\"test\",\"iss\":\"test-issuer\",\"exp\":null}");
  }

  @Test
  void shouldThrowExceptionIfJwtIsExpired() {
    String jwt = jwtWithExpiry(NOW.minusMillis(1));

    Throwable error = catchThrowable(() -> validator.validate(jwt));

    assertThat(error).isInstanceOf(JwtExpiredException.class).hasMessage("jwt expired %s", jwt);
  }

  @Test
  void shouldDoNothingIfJwtIsValidAndNotExpired() {
    String jwt = jwtWithExpiry(NOW);

    ThrowingCallable call = () -> validator.validate(jwt);

    assertThatCode(call).doesNotThrowAnyException();
  }

  private static String jwtWithExpiry(Instant expiry) {
    return JWT.create()
        .withSubject("test")
        .withIssuer("test-issuer")
        .withExpiresAt(expiry)
        .sign(Algorithm.HMAC256("test"));
  }
}
