package uk.co.mruoc.nac.repository.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.Game;

@RequiredArgsConstructor
@Slf4j
public class UpdateGameDao {

  private final PostgresGameConverter gameConverter;

  public void update(Connection connection, Game game) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("update game set game = ?::jsonb where id = ?::bigint;")) {
      statement.setString(1, gameConverter.toPostgresJson(game));
      statement.setLong(2, game.getId());
      statement.execute();
    }
  }
}
