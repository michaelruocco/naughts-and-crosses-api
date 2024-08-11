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
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.GamePage;
import uk.co.mruoc.nac.entities.GamePageRequest;

@RequiredArgsConstructor
@Slf4j
public class ReadGameDao {

  private final PostgresGameConverter gameConverter;
  private final GameQueryFactory queryFactory;

  public ReadGameDao(PostgresGameConverter gameConverter) {
    this(gameConverter, new GameQueryFactory());
  }

  public Optional<Game> findById(Connection connection, long id) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("select game_json from game where id = ?::bigint;")) {
      statement.setLong(1, id);
      return toGameIfPresent(statement);
    }
  }

  public Stream<Game> getAll(Connection connection) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("select game_json from game order by id desc;")) {
      return toGames(statement);
    }
  }

  public GamePage getPage(Connection connection, GamePageRequest request) throws SQLException {
    long count = getTotal(connection, request);
    try (PreparedStatement statement =
        connection.prepareStatement(queryFactory.toGetPageQuery(request))) {
      int index = 1;
      Optional<Boolean> complete = request.getComplete();
      if (complete.isPresent()) {
        statement.setBoolean(index++, complete.get());
      }
      Optional<String> username = request.getUsername();
      if (username.isPresent()) {
        statement.setString(index++, username.get());
      }
      statement.setLong(index++, request.getLimit());
      statement.setLong(index, request.getOffset());
      log.info("executing statement {}", statement);
      return toGamePage(statement, count);
    }
  }

  private long getTotal(Connection connection, GamePageRequest request) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement(queryFactory.toTotalQuery(request))) {
      int index = 1;
      Optional<Boolean> complete = request.getComplete();
      if (complete.isPresent()) {
        statement.setBoolean(index++, complete.get());
      }
      Optional<String> username = request.getUsername();
      if (username.isPresent()) {
        statement.setString(index, username.get());
      }
      return toTotal(statement);
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

  private GamePage toGamePage(PreparedStatement statement, long total) throws SQLException {
    return GamePage.builder().games(toGames(statement).toList()).total(total).build();
  }

  private long toTotal(PreparedStatement statement) throws SQLException {
    try (ResultSet rs = statement.executeQuery()) {
      rs.next();
      return rs.getLong(1);
    }
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
    return gameConverter.toGame(resultSet.getString("game_json"));
  }
}
