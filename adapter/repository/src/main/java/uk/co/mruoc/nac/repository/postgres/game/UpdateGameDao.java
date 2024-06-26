package uk.co.mruoc.nac.repository.postgres.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.Game;

@RequiredArgsConstructor
public class UpdateGameDao {

  private final PostgresGameConverter gameConverter;

  public void update(Connection connection, Game game) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("update game set game_json = ?::jsonb where id = ?::bigint;")) {
      statement.setString(1, gameConverter.toPostgresJson(game));
      statement.setLong(2, game.getId());
      statement.execute();
    }
  }
}
