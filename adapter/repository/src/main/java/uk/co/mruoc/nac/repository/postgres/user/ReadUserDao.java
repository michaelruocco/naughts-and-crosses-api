package uk.co.mruoc.nac.repository.postgres.user;

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
import uk.co.mruoc.nac.entities.User;

@RequiredArgsConstructor
@Slf4j
public class ReadUserDao {

  private final PostgresUserConverter userConverter;

  public Optional<User> findById(Connection connection, String id) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("select * from user_record where id = ?::varchar;")) {
      statement.setString(1, id);
      return toUserIfPresent(statement);
    }
  }

  public Stream<User> getAll(Connection connection) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("select * from user_record order by id asc;")) {
      return toUsers(statement);
    }
  }

  private Optional<User> toUserIfPresent(PreparedStatement statement) throws SQLException {
    try (var resultSet = statement.executeQuery()) {
      return toUserIfPresent(resultSet);
    }
  }

  private Optional<User> toUserIfPresent(ResultSet resultSet) throws SQLException {
    if (resultSet.next()) {
      return Optional.of(toUser(resultSet));
    }
    return Optional.empty();
  }

  private Stream<User> toUsers(PreparedStatement statement) throws SQLException {
    try (var resultSet = statement.executeQuery()) {
      return toUsers(resultSet);
    }
  }

  private Stream<User> toUsers(ResultSet resultSet) throws SQLException {
    Collection<User> users = new ArrayList<>();
    while (resultSet.next()) {
      users.add(toUser(resultSet));
    }
    return users.stream();
  }

  private User toUser(ResultSet resultSet) throws SQLException {
    return userConverter.toUser(resultSet.getString("user_json"));
  }
}
