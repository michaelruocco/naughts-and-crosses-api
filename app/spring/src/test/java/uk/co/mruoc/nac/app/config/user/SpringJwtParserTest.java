package uk.co.mruoc.nac.app.config.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import uk.co.mruoc.nac.user.JwtParser;
import uk.co.mruoc.nac.user.inmemory.InvalidJwtException;

class SpringJwtParserTest {

  private final JwtDecoder jwtDecoder = mock(JwtDecoder.class);

  private final JwtParser parser = new SpringJwtParser(jwtDecoder);

  @Test
  void shouldThrowExceptionIfTokenDoesNotHaveUsernameClaim() {
    String token = "test-token-1";
    Jwt jwt = givenJwtWithClaims(Collections.emptyMap());
    when(jwtDecoder.decode(token)).thenReturn(jwt);

    Throwable error = catchThrowable(() -> parser.toUsername(token));

    assertThat(error)
        .isInstanceOf(InvalidJwtException.class)
        .hasMessage("username claim not found");
  }

  @Test
  void shouldReturnUsernameIfTokenHasUsernameClaim() {
    String token = "test-token-2";
    String expectedUsername = "test-username";
    Jwt jwt = givenJwtWithClaims(Map.of("username", expectedUsername));
    when(jwtDecoder.decode(token)).thenReturn(jwt);

    String username = parser.toUsername(token);

    assertThat(username).isEqualTo(expectedUsername);
  }

  @Test
  void shouldReturnExpiry() {
    String token = "test-token-2";
    Instant expectedExpiry = Instant.now();
    Jwt jwt = givenJwtWithExpiry(expectedExpiry);
    when(jwtDecoder.decode(token)).thenReturn(jwt);

    Instant expiry = parser.toExpiry(token);

    assertThat(expiry).isEqualTo(expectedExpiry);
  }

  private Jwt givenJwtWithClaims(Map<String, Object> claims) {
    Jwt jwt = mock(Jwt.class);
    when(jwt.getClaims()).thenReturn(claims);
    return jwt;
  }

  private Jwt givenJwtWithExpiry(Instant expiry) {
    Jwt jwt = mock(Jwt.class);
    when(jwt.getExpiresAt()).thenReturn(expiry);
    return jwt;
  }
}
