package uk.co.mruoc.nac.repository.postgres;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.repository.GameRepositoryException;
import uk.co.mruoc.nac.usecases.IdSupplier;

@RequiredArgsConstructor
@Slf4j
public class PostgresIdSupplier implements IdSupplier {

  private final DataSource dataSource;
  private final IdDao dao;

  public PostgresIdSupplier(DataSource dataSource) {
    this(dataSource, new IdDao());
  }

  @Override
  public void reset() {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      dao.resetNextId(connection);
    } catch (SQLException e) {
      throw new GameRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("reset next id took {}ms", duration.toMillis());
    }
  }

  @Override
  public long getAsLong() {
    Instant start = Instant.now();
    try (var connection = dataSource.getConnection()) {
      long id = dao.getNextId(connection);
      log.info("returning next id {}", id);
      return id;
    } catch (SQLException e) {
      throw new GameRepositoryException(e);
    } finally {
      var duration = Duration.between(start, Instant.now());
      log.info("get next id took {}ms", duration.toMillis());
    }
  }
}
