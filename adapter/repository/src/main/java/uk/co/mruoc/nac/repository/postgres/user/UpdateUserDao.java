package uk.co.mruoc.nac.repository.postgres.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.User;

@RequiredArgsConstructor
public class UpdateUserDao {

  private final PostgresUserConverter userConverter;

  public void update(Connection connection, User user) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement(
            "update user_record set user_json = ?::jsonb where username = ?::varchar;")) {
      statement.setString(1, userConverter.toPostgresJson(user));
      statement.setString(2, user.getUsername());
      statement.execute();
    }
  }
}
