package uk.co.mruoc.nac.repository.postgres.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.Game;

@RequiredArgsConstructor
public class CreateGameDao {

  private final PostgresGameConverter gameConverter;

  public void create(Connection connection, Game game) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("insert into game (id, game) values (?::bigint, ?::jsonb);")) {
      statement.setLong(1, game.getId());
      statement.setString(2, gameConverter.toPostgresJson(game));
      statement.execute();
    }
  }
}
