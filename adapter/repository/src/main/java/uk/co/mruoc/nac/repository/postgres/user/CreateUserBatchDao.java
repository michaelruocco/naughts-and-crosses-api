package uk.co.mruoc.nac.repository.postgres.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.UserBatch;

@RequiredArgsConstructor
public class CreateUserBatchDao {

  private final PostgresUserBatchConverter batchConverter;

  public void create(Connection connection, UserBatch batch) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement(
            "insert into user_batch (id, created_at, batch_json) values (?::varchar, ?::timestamp, ?::jsonb);")) {
      statement.setString(1, batch.getId());
      statement.setTimestamp(2, Timestamp.from(batch.getCreatedAt()));
      statement.setString(3, batchConverter.toPostgresJson(batch));
      statement.execute();
    }
  }
}
