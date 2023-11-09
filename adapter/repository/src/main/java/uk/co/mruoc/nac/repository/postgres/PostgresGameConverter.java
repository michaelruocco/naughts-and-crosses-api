package uk.co.mruoc.nac.repository.postgres;

import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PGobject;
import uk.co.mruoc.json.JsonConverter;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.repository.postgres.converter.DbGameConverter;
import uk.co.mruoc.nac.repository.postgres.dto.DbGame;

@RequiredArgsConstructor
@Slf4j
public class PostgresGameConverter {

  private final JsonConverter jsonConverter;
  private final DbGameConverter gameConverter;

  public PostgresGameConverter(JsonConverter jsonConverter) {
    this(jsonConverter, new DbGameConverter());
  }

  public String toPostgresJson(Game game) throws SQLException {
    var object = new PGobject();
    object.setType("json");
    object.setValue(toJson(game));
    return object.toString();
  }

  public Game toGame(String json) {
    DbGame dbGame = jsonConverter.toObject(json, DbGame.class);
    return gameConverter.toGame(dbGame);
  }

  private String toJson(Game game) {
    DbGame dbGame = gameConverter.toDbGame(game);
    return jsonConverter.toJson(dbGame);
  }
}
