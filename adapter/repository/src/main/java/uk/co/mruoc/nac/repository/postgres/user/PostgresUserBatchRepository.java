package uk.co.mruoc.nac.repository.postgres.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.UserBatch;
import uk.co.mruoc.nac.usecases.UserBatchRepository;

@RequiredArgsConstructor
public class PostgresUserBatchRepository implements UserBatchRepository {

  private final Map<String, UserBatch> batches;

  public PostgresUserBatchRepository() {
    this(new HashMap<>());
  }

  @Override
  public Optional<UserBatch> get(String id) {
    return Optional.ofNullable(batches.get(id));
  }

  @Override
  public void create(UserBatch batch) {
    save(batch);
  }

  @Override
  public void update(UserBatch batch) {
    save(batch);
  }

  private void save(UserBatch batch) {
    batches.put(batch.getId(), batch);
  }
}