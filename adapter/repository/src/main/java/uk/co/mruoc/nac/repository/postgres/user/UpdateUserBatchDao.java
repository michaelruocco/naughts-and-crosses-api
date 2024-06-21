package uk.co.mruoc.nac.repository.postgres.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.UserBatch;

@RequiredArgsConstructor
public class UpdateUserBatchDao {

  private final PostgresUserBatchConverter batchConverter;

  public void update(Connection connection, UserBatch batch) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement(
            "update user_batch set batch_json = ?::jsonb where id = ?::varchar;")) {
      statement.setString(1, batchConverter.toPostgresJson(batch));
      statement.setString(2, batch.getId());
      statement.execute();
    }
  }
}
