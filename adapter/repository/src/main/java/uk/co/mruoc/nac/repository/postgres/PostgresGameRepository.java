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
  private final ReadGameDao readGameDao;
  private final DeleteGameDao deleteGameDao;
  private final UpsertGameDao upsertGameDao;

  public PostgresGameRepository(DataSource dataSource, JsonConverter jsonConverter) {
    this(dataSource, new PostgresGameConverter(jsonConverter));
  }

  public PostgresGameRepository(DataSource dataSource, PostgresGameConverter gameConverter) {
    this(
        dataSource,
        new ReadGameDao(gameConverter),
        new DeleteGameDao(),
        new UpsertGameDao(gameConverter));
  }

  @Override
  public Optional<Game> find(long id) {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      return readGameDao.findById(connection, id);
    } catch (SQLException e) {
      throw new GameRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("find game by id {} took {}ms", id, duration.toMillis());
    }
  }

  @Override
  public void save(Game game) {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      upsertGameDao.upsert(connection, game);
    } catch (SQLException e) {
      throw new GameRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("save game with id {} took {}ms", game.getId(), duration.toMillis());
    }
  }

  @Override
  public Stream<Game> getAll() {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      return readGameDao.getAll(connection);
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
      deleteGameDao.deleteAll(connection);
    } catch (SQLException e) {
      throw new GameRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("delete all games took {}ms", duration.toMillis());
    }
  }
}
