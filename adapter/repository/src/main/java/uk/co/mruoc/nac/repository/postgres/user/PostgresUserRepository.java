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
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.repository.UserRepositoryException;
import uk.co.mruoc.nac.usecases.UserRepository;

@RequiredArgsConstructor
@Slf4j
// TODO refactor to use normal postgres sql table rather than using json to store all fields
public class PostgresUserRepository implements UserRepository {

  private final DataSource dataSource;
  private final CreateUserDao createDao;
  private final ReadUserDao readDao;
  private final UpdateUserDao updateDao;
  private final DeleteUserDao deleteDao;

  public PostgresUserRepository(DataSource dataSource, JsonConverter jsonConverter) {
    this(dataSource, new PostgresUserConverter(jsonConverter));
  }

  public PostgresUserRepository(DataSource dataSource, PostgresUserConverter userConverter) {
    this(
        dataSource,
        new CreateUserDao(userConverter),
        new ReadUserDao(userConverter),
        new UpdateUserDao(userConverter),
        new DeleteUserDao());
  }

  @Override
  public void create(User user) {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      createDao.create(connection, user);
    } catch (SQLException e) {
      throw new UserRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("create user with id {} took {}ms", user.getId(), duration.toMillis());
    }
  }

  @Override
  public Optional<User> getByUsername(String username) {
    return getAll().filter(user -> user.hasUsername(username)).findFirst();
  }

  @Override
  public Optional<User> getById(String id) {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      return readDao.findById(connection, id);
    } catch (SQLException e) {
      throw new UserRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("find user by id {} took {}ms", id, duration.toMillis());
    }
  }

  @Override
  public void update(User user) {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      updateDao.update(connection, user);
    } catch (SQLException e) {
      throw new UserRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("update user with id {} took {}ms", user.getId(), duration.toMillis());
    }
  }

  @Override
  public Stream<User> getAll() {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      return readDao.getAll(connection);
    } catch (SQLException e) {
      throw new UserRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("get all games took {}ms", duration.toMillis());
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
      log.info("delete all users took {}ms", duration.toMillis());
    }
  }

  @Override
  public void delete(String id) {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      deleteDao.delete(connection, id);
    } catch (SQLException e) {
      throw new UserRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("delete user with id {} took {}ms", id, duration.toMillis());
    }
  }
}
