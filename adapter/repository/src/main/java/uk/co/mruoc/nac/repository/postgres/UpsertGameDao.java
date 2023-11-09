package uk.co.mruoc.nac.repository.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.Game;

@RequiredArgsConstructor
@Slf4j
public class UpsertGameDao {

  private final PostgresGameConverter gameConverter;

  public void upsert(Connection connection, Game game) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement(
            "insert into game (id, game) values (?::bigint, ?::jsonb) on conflict (id) do update set game = EXCLUDED.game;")) {
      statement.setLong(1, game.getId());
      statement.setString(2, gameConverter.toPostgresJson(game));
      statement.execute();
    }
  }
}
