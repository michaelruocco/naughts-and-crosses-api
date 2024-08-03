package uk.co.mruoc.nac.user.inmemory;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.json.JsonConverter;

@RequiredArgsConstructor
public class JsonFieldExtractor {

  private final JsonConverter jsonConverter;

  public Optional<String> toUsername(String json) {
    JsonNode node = toJsonNode(json);
    return extractNode(node, "username").map(JsonNode::asText);
  }

  public Optional<Instant> toExpiry(String json) {
    JsonNode node = toJsonNode(json);
    return extractNode(node, "exp").map(JsonNode::asInt).map(Instant::ofEpochSecond);
  }

  private JsonNode toJsonNode(String body) {
    return jsonConverter.toObject(body, JsonNode.class);
  }

  private static Optional<JsonNode> extractNode(JsonNode node, String fieldName) {
    JsonNode fieldNode = node.get(fieldName);
    if (isNull(fieldNode)) {
      return Optional.empty();
    }
    return Optional.of(fieldNode);
  }

  private static boolean isNull(JsonNode node) {
    return Objects.isNull(node) || node.isNull();
  }
}
