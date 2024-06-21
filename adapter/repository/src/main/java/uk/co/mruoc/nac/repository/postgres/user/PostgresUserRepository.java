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
      log.info("create user {} took {}ms", user.getUsername(), duration.toMillis());
    }
  }

  @Override
  public Optional<User> getByUsername(String username) {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      return readDao.findByUsername(connection, username);
    } catch (SQLException e) {
      throw new UserRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("find user {} took {}ms", username, duration.toMillis());
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
      log.info("update user {} took {}ms", user.getUsername(), duration.toMillis());
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
      log.info("get all users took {}ms", duration.toMillis());
    }
  }

  @Override
  public void delete(String username) {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      deleteDao.delete(connection, username);
    } catch (SQLException e) {
      throw new UserRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("delete user {} took {}ms", username, duration.toMillis());
    }
  }
}
