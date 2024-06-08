package uk.co.mruoc.nac.repository.postgres.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.User;

@RequiredArgsConstructor
@Slf4j
public class CreateUserDao {

  private final PostgresUserConverter userConverter;

  public void create(Connection connection, User user) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement(
            "insert into user_record (id, user_json) values (?::varchar, ?::jsonb);")) {
      statement.setString(1, user.getId());
      statement.setString(2, userConverter.toPostgresJson(user));
      statement.execute();
    }
  }
}
