package uk.co.mruoc.nac.user.inmemory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class StubJwtParserTest {

  private static final String VALID_JWT = "e30=.Ym9keQ==.e30=";
  private static final String INVALID_JWT = "invalid-jwt";
  private static final String DECODED_BODY = "body";

  private final JsonFieldExtractor fieldExtractor = mock(JsonFieldExtractor.class);

  private final StubJwtParser parser = new StubJwtParser(fieldExtractor);

  @Test
  void toUsernameShouldThrowExceptionIfJwtDoesNotContainAtLeastTwoChunks() {
    Throwable error = catchThrowable(() -> parser.toUsername(INVALID_JWT));

    assertThat(error)
        .isInstanceOf(InvalidJwtException.class)
        .hasMessage("jwt %s does not contain body chunk", INVALID_JWT);
  }

  @Test
  void toExpiryShouldThrowExceptionIfJwtDoesNotContainAtLeastTwoChunks() {
    Throwable error = catchThrowable(() -> parser.toExpiry(INVALID_JWT));

    assertThat(error)
        .isInstanceOf(InvalidJwtException.class)
        .hasMessage("jwt %s does not contain body chunk", INVALID_JWT);
  }

  @Test
  void shouldReturnUsernameFromBodyIfPresent() {
    String expectedUsername = "some-username";
    when(fieldExtractor.toUsername(DECODED_BODY)).thenReturn(Optional.of(expectedUsername));

    String username = parser.toUsername(VALID_JWT);

    assertThat(username).isEqualTo(expectedUsername);
  }

  @Test
  void shouldThrowExceptionIfUsernameIsNotPresentInBody() {
    when(fieldExtractor.toUsername(DECODED_BODY)).thenReturn(Optional.empty());

    Throwable error = catchThrowable(() -> parser.toUsername(VALID_JWT));

    assertThat(error)
        .isInstanceOf(InvalidJwtException.class)
        .hasMessage("username not found in body %s", DECODED_BODY);
  }

  @Test
  void shouldReturnExpiryFromBodyIfPresent() {
    Instant expectedExpiry = Instant.now();
    when(fieldExtractor.toExpiry(DECODED_BODY)).thenReturn(Optional.of(expectedExpiry));

    Instant expiry = parser.toExpiry(VALID_JWT);

    assertThat(expiry).isEqualTo(expectedExpiry);
  }

  @Test
  void shouldThrowExceptionIfExpiryIsNotPresentInBody() {
    when(fieldExtractor.toExpiry(DECODED_BODY)).thenReturn(Optional.empty());

    Throwable error = catchThrowable(() -> parser.toExpiry(VALID_JWT));

    assertThat(error)
        .isInstanceOf(InvalidJwtException.class)
        .hasMessage("expiry not found in body %s", DECODED_BODY);
  }
}
