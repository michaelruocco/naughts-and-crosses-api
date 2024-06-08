package uk.co.mruoc.nac.repository.postgres.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUserDao {

  public void deleteAll(Connection connection) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement("delete from user_record;")) {
      statement.execute();
    }
  }

  public void delete(Connection connection, String id) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("delete from user_record where id = ?::varchar;")) {
      statement.setString(1, id);
      statement.execute();
    }
  }
}
