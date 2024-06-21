package uk.co.mruoc.nac.repository.postgres.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.User;

@RequiredArgsConstructor
public class CreateUserDao {

  private final PostgresUserConverter userConverter;

  public void create(Connection connection, User user) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement(
            "insert into user_record (username, user_json) values (?::varchar, ?::jsonb);")) {
      statement.setString(1, user.getUsername());
      statement.setString(2, userConverter.toPostgresJson(user));
      statement.execute();
    }
  }
}
