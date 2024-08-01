package uk.co.mruoc.nac.user.inmemory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.user.JwtParser;

@RequiredArgsConstructor
public class StubJwtParser implements JwtParser {

  private final ObjectMapper mapper;
  private final Base64.Decoder decoder;

  public StubJwtParser(ObjectMapper mapper) {
    this(mapper, Base64.getDecoder());
  }

  @Override
  public String toUsername(String token) {
    System.out.println("mruoc decoding token " + token);
    JsonNode body = extractBodyAsJsonNode(token);
    System.out.println("mruoc decoded body " + body.toString());
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
    try {
      return mapper.readTree(body);
    } catch (JsonProcessingException e) {
      throw new InvalidJwtException(e);
    }
  }

  private String toUsername(JsonNode body) {
    JsonNode username = body.get("username");
    if (username.isNull()) {
      throw new InvalidJwtException(String.format("username not found in body %s", body));
    }
    return username.asText();
  }

  private Instant toExpiry(JsonNode body) {
    JsonNode expiry = body.get("exp");
    if (expiry.isNull()) {
      throw new InvalidJwtException(String.format("expiry not found in body %s", body));
    }
    return Instant.ofEpochSecond(expiry.asInt());
  }
}
