package uk.co.mruoc.nac.repository.postgres.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.UserBatch;

@RequiredArgsConstructor
public class ReadUserBatchDao {

  private final PostgresUserBatchConverter batchConverter;

  public Optional<UserBatch> findById(Connection connection, String id) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("select batch_json from user_batch where id = ?::varchar;")) {
      statement.setString(1, id);
      return toUserBatchIfPresent(statement);
    }
  }

  public Stream<UserBatch> getAll(Connection connection) throws SQLException {
    try (PreparedStatement statement =
        connection.prepareStatement("select batch_json from user_batch order by created_at asc;")) {
      return toUserBatches(statement);
    }
  }

  private Optional<UserBatch> toUserBatchIfPresent(PreparedStatement statement)
      throws SQLException {
    try (var resultSet = statement.executeQuery()) {
      return toUserBatchIfPresent(resultSet);
    }
  }

  private Optional<UserBatch> toUserBatchIfPresent(ResultSet resultSet) throws SQLException {
    if (resultSet.next()) {
      return Optional.of(toUserBatch(resultSet));
    }
    return Optional.empty();
  }

  private Stream<UserBatch> toUserBatches(PreparedStatement statement) throws SQLException {
    try (var resultSet = statement.executeQuery()) {
      return toUserBatches(resultSet);
    }
  }

  private Stream<UserBatch> toUserBatches(ResultSet resultSet) throws SQLException {
    Collection<UserBatch> batches = new ArrayList<>();
    while (resultSet.next()) {
      batches.add(toUserBatch(resultSet));
    }
    return batches.stream();
  }

  private UserBatch toUserBatch(ResultSet resultSet) throws SQLException {
    return batchConverter.toBatch(resultSet.getString("batch_json"));
  }
}
