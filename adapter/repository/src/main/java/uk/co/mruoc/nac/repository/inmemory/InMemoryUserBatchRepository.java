package uk.co.mruoc.nac.repository.inmemory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.UserBatch;
import uk.co.mruoc.nac.usecases.UserBatchRepository;

@RequiredArgsConstructor
public class InMemoryUserBatchRepository implements UserBatchRepository {

  private final Map<String, UserBatch> batches;

  public InMemoryUserBatchRepository() {
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
