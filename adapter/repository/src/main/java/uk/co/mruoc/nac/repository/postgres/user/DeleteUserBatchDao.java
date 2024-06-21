package uk.co.mruoc.nac.repository.postgres.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUserBatchDao {

  public void deleteAll(Connection connection) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement("delete from user_batch;")) {
      statement.execute();
    }
  }
}
