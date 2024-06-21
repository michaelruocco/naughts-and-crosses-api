package uk.co.mruoc.nac.repository.postgres.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUserDao {

  public void delete(Connection connection, String username) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("delete from user_record where username = ?::varchar;")) {
      statement.setString(1, username);
      statement.execute();
    }
  }
}
