package uk.co.mruoc.nac.repository.postgres.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteGameDao {

  public void deleteAll(Connection connection) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement("delete from game;")) {
      statement.execute();
    }
  }

  public void delete(Connection connection, long id) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("delete from game where id = ?::bigint;")) {
      statement.setLong(1, id);
      statement.execute();
    }
  }
}
