package uk.co.mruoc.nac.user.inmemory;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.json.jackson.JacksonJsonConverter;

class JsonFieldExtractorTest {

  private final JsonFieldExtractor extractor =
      new JsonFieldExtractor(new JacksonJsonConverter(new ObjectMapper()));

  @Test
  void shouldReturnUsernameIfPresent() {
    String expectedUsername = "mruocco";
    String json = String.format("{\"username\": \"%s\"}", expectedUsername);

    Optional<String> username = extractor.toUsername(json);

    assertThat(username).contains(expectedUsername);
  }

  @Test
  void shouldReturnEmptyIfUsernameIsNull() {
    String json = "{\"username\": null}";

    Optional<String> username = extractor.toUsername(json);

    assertThat(username).isEmpty();
  }

  @Test
  void shouldEmptyIfUsernameNotPresent() {
    String json = "{}";

    Optional<String> username = extractor.toUsername(json);

    assertThat(username).isEmpty();
  }

  @Test
  void shouldReturnExpiryIfPresent() {
    long expectedExpirySeconds = 12345678;
    String json = String.format("{\"exp\": %d}", expectedExpirySeconds);

    Optional<Instant> expiry = extractor.toExpiry(json);

    assertThat(expiry).map(Instant::getEpochSecond).contains(expectedExpirySeconds);
  }

  @Test
  void shouldReturnEmptyIfExpiryIsNull() {
    String json = "{\"exp\": null}";

    Optional<Instant> expiry = extractor.toExpiry(json);

    assertThat(expiry).isEmpty();
  }

  @Test
  void shouldEmptyIfExpiryNotPresent() {
    String json = "{}";

    Optional<Instant> expiry = extractor.toExpiry(json);

    assertThat(expiry).isEmpty();
  }
}
