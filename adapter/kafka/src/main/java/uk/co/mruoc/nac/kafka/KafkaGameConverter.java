package uk.co.mruoc.nac.kafka;

import lombok.Builder;
import uk.co.mruoc.json.JsonConverter;
import uk.co.mruoc.nac.api.converter.ApiGameConverter;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.entities.Game;

@Builder
public class KafkaGameConverter {

  private final ApiGameConverter apiGameConverter;
  private final JsonConverter jsonConverter;

  public String toJson(Game game) {
    ApiGame apiGame = apiGameConverter.toApiGame(game);
    return jsonConverter.toJson(apiGame);
  }

  public Game toGame(String json) {
    jsonConverter.toObject(json, ApiGame.class);
    ApiGame apiGame = jsonConverter.toObject(json, ApiGame.class);
    return apiGameConverter.toGame(apiGame);
  }
}
