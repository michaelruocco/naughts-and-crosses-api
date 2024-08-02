package uk.co.mruoc.nac.user.inmemory;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.json.JsonConverter;
import uk.co.mruoc.nac.user.JwtParser;

@RequiredArgsConstructor
public class StubJwtParser implements JwtParser {

  private final JsonConverter jsonConverter;
  private final Base64.Decoder decoder;

  public StubJwtParser(JsonConverter jsonConverter) {
    this(jsonConverter, Base64.getDecoder());
  }

  @Override
  public String toUsername(String token) {
    JsonNode body = extractBodyAsJsonNode(token);
    return toUsername(body);
  }

  @Override
  public Instant toExpiry(String token) {
    JsonNode body = extractBodyAsJsonNode(token);
    return toExpiry(body);
  }

  private JsonNode extractBodyAsJsonNode(String token) {
    return toJsonNode(extractBody(token));
  }

  private String extractBody(String token) {
    String[] parts = token.split("\\.");
    if (parts.length < 2) {
      throw new InvalidJwtException(token);
    }
    return new String(decoder.decode(parts[1]));
  }

  private JsonNode toJsonNode(String body) {
    return jsonConverter.toObject(body, JsonNode.class);
  }

  private String toUsername(JsonNode body) {
    JsonNode username = body.get("username");
    if (isNull(username)) {
      throw new InvalidJwtException(String.format("username not found in body %s", body));
    }
    return username.asText();
  }

  private Instant toExpiry(JsonNode body) {
    JsonNode expiry = body.get("exp");
    if (isNull(expiry)) {
      throw new InvalidJwtException(String.format("expiry not found in body %s", body));
    }
    return Instant.ofEpochSecond(expiry.asInt());
  }

  private static boolean isNull(JsonNode node) {
    return Objects.isNull(node) || node.isNull();
  }
}
