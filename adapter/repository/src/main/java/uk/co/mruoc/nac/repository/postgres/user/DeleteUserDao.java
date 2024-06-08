package uk.co.mruoc.nac.repository.postgres.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
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
