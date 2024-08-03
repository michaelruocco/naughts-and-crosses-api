package uk.co.mruoc.nac.user.inmemory;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.usecases.JwtExpiredException;
import uk.co.mruoc.nac.user.JwtParser;

class JwtValidatorTest {

  private static final Instant NOW = Instant.parse("2024-07-12T20:25:01Z");

  private final Clock clock = Clock.fixed(NOW, UTC);
  private final JwtParser jwtParser = mock(JwtParser.class);

  private final JwtValidator validator = new JwtValidator(clock, jwtParser);

  @Test
  void shouldThrowExceptionIfJwtIsExpired() {
    String jwt = "test-token-1";
    when(jwtParser.toExpiry(jwt)).thenReturn(NOW.minusMillis(1));

    Throwable error = catchThrowable(() -> validator.validate(jwt));

    assertThat(error).isInstanceOf(JwtExpiredException.class).hasMessage("jwt expired %s", jwt);
  }

  @Test
  void shouldDoNothingIfJwtIsValidAndNotExpired() {
    String jwt = "test-token-2";
    when(jwtParser.toExpiry(jwt)).thenReturn(NOW);

    ThrowingCallable call = () -> validator.validate(jwt);

    assertThatCode(call).doesNotThrowAnyException();
  }
}
