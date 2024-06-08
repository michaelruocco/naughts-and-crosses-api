package uk.co.mruoc.nac.repository.postgres.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.User;

@RequiredArgsConstructor
@Slf4j
public class UpdateUserDao {

  private final PostgresUserConverter userConverter;

  public void update(Connection connection, User user) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement(
            "update user_record set user_json = ?::jsonb where id = ?::varchar;")) {
      statement.setString(1, userConverter.toPostgresJson(user));
      statement.setString(2, user.getId());
      statement.execute();
    }
  }
}
