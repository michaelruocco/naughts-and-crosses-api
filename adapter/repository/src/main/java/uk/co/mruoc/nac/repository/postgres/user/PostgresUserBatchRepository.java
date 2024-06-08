package uk.co.mruoc.nac.repository.postgres.user;

import java.util.Optional;
import uk.co.mruoc.nac.entities.UserBatch;
import uk.co.mruoc.nac.usecases.UserBatchRepository;

public class PostgresUserBatchRepository implements UserBatchRepository {
  @Override
  public Optional<UserBatch> get(String id) {
    return Optional.empty();
  }

  @Override
  public void save(UserBatch batch) {}
}
