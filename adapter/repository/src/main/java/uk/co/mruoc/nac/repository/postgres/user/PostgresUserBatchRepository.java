package uk.co.mruoc.nac.repository.postgres.user;

import java.util.Optional;
import uk.co.mruoc.nac.entities.UserBatch;
import uk.co.mruoc.nac.usecases.UserBatchRepository;

public class PostgresUserBatchRepository implements UserBatchRepository {
  @Override
  public Optional<UserBatch> get(String id) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void save(UserBatch batch) {
    throw new UnsupportedOperationException();
  }
}
