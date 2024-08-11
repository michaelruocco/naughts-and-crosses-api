package uk.co.mruoc.nac.repository.postgres.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.Game;

@RequiredArgsConstructor
public class CreateGameDao {

  private final PostgresGameConverter gameConverter;

  public void create(Connection connection, Game game) throws SQLException {
    connection.setAutoCommit(false);
    try {
      insertGame(connection, game);
      insertGamePlayers(connection, game);
      connection.commit();
    } catch (SQLException e) {
      connection.rollback();
      throw e;
    }
  }

  private void insertGame(Connection connection, Game game) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement(
            "insert into game (id, game_json, complete) values (?::bigint, ?::jsonb, ?::boolean);")) {
      statement.setLong(1, game.getId());
      statement.setString(2, gameConverter.toPostgresJson(game));
      statement.setBoolean(3, game.isComplete());
      statement.execute();
    }
  }

  private void insertGamePlayers(Connection connection, Game game) throws SQLException {
    Collection<String> usernames = game.getPlayerUsernames().distinct().toList();
    for (String username : usernames) {
      insertGamePlayer(connection, game.getId(), username);
    }
  }

  private void insertGamePlayer(Connection connection, long gameId, String username)
      throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement(
            "insert into game_player (game_id, username) values (?::bigint, ?::varchar);")) {
      statement.setLong(1, gameId);
      statement.setString(2, username);
      statement.execute();
    }
  }
}
