package uk.co.mruoc.nac.repository.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.repository.GameRepositoryException;

@RequiredArgsConstructor
@Slf4j
public class IdDao {

  public long getNextId(Connection connection) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement("select nextval('game_id');")) {
      return toGameId(statement);
    }
  }

  public void resetNextId(Connection connection) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("alter sequence game_id restart with 1;")) {
      statement.execute();
    }
  }

  private long toGameId(PreparedStatement statement) throws SQLException {
    try (var resultSet = statement.executeQuery()) {
      return toGameId(resultSet);
    }
  }

  private long toGameId(ResultSet resultSet) throws SQLException {
    if (resultSet.next()) {
      return resultSet.getLong(1);
    }
    throw new GameRepositoryException("unable to generate next id");
  }
}
