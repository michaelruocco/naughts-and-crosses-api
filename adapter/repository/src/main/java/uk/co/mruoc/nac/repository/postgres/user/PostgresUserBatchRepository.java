package uk.co.mruoc.nac.repository.postgres.user;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.json.JsonConverter;
import uk.co.mruoc.nac.entities.UserBatch;
import uk.co.mruoc.nac.repository.UserRepositoryException;
import uk.co.mruoc.nac.usecases.UserBatchRepository;

@RequiredArgsConstructor
@Slf4j
public class PostgresUserBatchRepository implements UserBatchRepository {

  private final DataSource dataSource;
  private final CreateUserBatchDao createDao;
  private final ReadUserBatchDao readDao;
  private final UpdateUserBatchDao updateDao;
  private final DeleteUserBatchDao deleteDao;

  public PostgresUserBatchRepository(DataSource dataSource, JsonConverter jsonConverter) {
    this(dataSource, new PostgresUserBatchConverter(jsonConverter));
  }

  public PostgresUserBatchRepository(
      DataSource dataSource, PostgresUserBatchConverter batchConverter) {
    this(
        dataSource,
        new CreateUserBatchDao(batchConverter),
        new ReadUserBatchDao(batchConverter),
        new UpdateUserBatchDao(batchConverter),
        new DeleteUserBatchDao());
  }

  @Override
  public Optional<UserBatch> get(String id) {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      return readDao.findById(connection, id);
    } catch (SQLException e) {
      throw new UserRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("find user batch {} took {}ms", id, duration.toMillis());
    }
  }

  @Override
  public void create(UserBatch batch) {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      createDao.create(connection, batch);
    } catch (SQLException e) {
      throw new UserRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("create user batch {} took {}ms", batch.getId(), duration.toMillis());
    }
  }

  @Override
  public void update(UserBatch batch) {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      updateDao.update(connection, batch);
    } catch (SQLException e) {
      throw new UserRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("update user batch {} took {}ms", batch.getId(), duration.toMillis());
    }
  }

  @Override
  public Stream<UserBatch> getAll() {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      return readDao.getAll(connection);
    } catch (SQLException e) {
      throw new UserRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("get all user batches took {}ms", duration.toMillis());
    }
  }

  @Override
  public void deleteAll() {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      deleteDao.deleteAll(connection);
    } catch (SQLException e) {
      throw new UserRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("delete all user batches took {}ms", duration.toMillis());
    }
  }
}
