package uk.co.mruoc.nac.repository.postgres.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.Game;

@RequiredArgsConstructor
public class ReadGameDao {

  private final PostgresGameConverter gameConverter;

  public Optional<Game> findById(Connection connection, long id) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("select game from game where id = ?::bigint;")) {
      statement.setLong(1, id);
      return toGameIfPresent(statement);
    }
  }

  public Stream<Game> getAll(Connection connection) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("select game from game order by id asc;")) {
      return toGames(statement);
    }
  }

  private Optional<Game> toGameIfPresent(PreparedStatement statement) throws SQLException {
    try (var resultSet = statement.executeQuery()) {
      return toGameIfPresent(resultSet);
    }
  }

  private Optional<Game> toGameIfPresent(ResultSet resultSet) throws SQLException {
    if (resultSet.next()) {
      return Optional.of(toGame(resultSet));
    }
    return Optional.empty();
  }

  private Stream<Game> toGames(PreparedStatement statement) throws SQLException {
    try (var resultSet = statement.executeQuery()) {
      return toGames(resultSet);
    }
  }

  private Stream<Game> toGames(ResultSet resultSet) throws SQLException {
    Collection<Game> games = new ArrayList<>();
    while (resultSet.next()) {
      games.add(toGame(resultSet));
    }
    return games.stream();
  }

  private Game toGame(ResultSet resultSet) throws SQLException {
    return gameConverter.toGame(resultSet.getString("game"));
  }
}
