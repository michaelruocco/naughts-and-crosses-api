package uk.co.mruoc.nac.repository.postgres;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.json.JsonConverter;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.repository.GameRepositoryException;
import uk.co.mruoc.nac.usecases.GameRepository;

@RequiredArgsConstructor
@Slf4j
public class PostgresGameRepository implements GameRepository {

  private final DataSource dataSource;
  private final CreateGameDao createDao;
  private final ReadGameDao readDao;
  private final UpdateGameDao updateDao;
  private final DeleteGameDao deleteDao;

  public PostgresGameRepository(DataSource dataSource, JsonConverter jsonConverter) {
    this(dataSource, new PostgresGameConverter(jsonConverter));
  }

  public PostgresGameRepository(DataSource dataSource, PostgresGameConverter gameConverter) {
    this(
        dataSource,
        new CreateGameDao(gameConverter),
        new ReadGameDao(gameConverter),
        new UpdateGameDao(gameConverter),
        new DeleteGameDao());
  }

  @Override
  public void create(Game game) {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      createDao.create(connection, game);
    } catch (SQLException e) {
      throw new GameRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("create game with id {} took {}ms", game.getId(), duration.toMillis());
    }
  }

  @Override
  public Optional<Game> get(long id) {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      return readDao.findById(connection, id);
    } catch (SQLException e) {
      throw new GameRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("find game by id {} took {}ms", id, duration.toMillis());
    }
  }

  @Override
  public void update(Game game) {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      updateDao.update(connection, game);
    } catch (SQLException e) {
      throw new GameRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("update game with id {} took {}ms", game.getId(), duration.toMillis());
    }
  }

  @Override
  public Stream<Game> getAll() {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      return readDao.getAll(connection);
    } catch (SQLException e) {
      throw new GameRepositoryException(e);
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
      throw new GameRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("delete all games took {}ms", duration.toMillis());
    }
  }

  @Override
  public void delete(long id) {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      deleteDao.delete(connection, id);
    } catch (SQLException e) {
      throw new GameRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("delete game with id {} took {}ms", id, duration.toMillis());
    }
  }
}
