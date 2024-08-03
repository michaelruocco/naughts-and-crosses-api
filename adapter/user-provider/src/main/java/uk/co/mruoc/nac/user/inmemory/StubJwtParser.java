package uk.co.mruoc.nac.user.inmemory;

import java.time.Instant;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.json.JsonConverter;
import uk.co.mruoc.nac.user.JwtParser;

@RequiredArgsConstructor
public class StubJwtParser implements JwtParser {

  private final JsonFieldExtractor fieldExtractor;
  private final Base64.Decoder decoder;

  public StubJwtParser(JsonConverter jsonConverter) {
    this(new JsonFieldExtractor(jsonConverter));
  }

  public StubJwtParser(JsonFieldExtractor fieldExtractor) {
    this(fieldExtractor, Base64.getDecoder());
  }

  @Override
  public String toUsername(String jwt) {
    String body = extractBody(jwt);
    return fieldExtractor
        .toUsername(body)
        .orElseThrow(
            () -> new InvalidJwtException(String.format("username not found in body %s", body)));
  }

  @Override
  public Instant toExpiry(String jwt) {
    String body = extractBody(jwt);
    return fieldExtractor
        .toExpiry(body)
        .orElseThrow(
            () -> new InvalidJwtException(String.format("expiry not found in body %s", body)));
  }

  private String extractBody(String jwt) {
    String[] chunks = jwt.split("\\.");
    if (chunks.length < 2) {
      throw new InvalidJwtException(String.format("jwt %s does not contain body chunk", jwt));
    }
    return new String(decoder.decode(chunks[1]));
  }
}
