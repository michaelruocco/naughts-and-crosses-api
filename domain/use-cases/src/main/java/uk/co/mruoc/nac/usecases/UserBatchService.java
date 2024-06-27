package uk.co.mruoc.nac.usecases;

import java.util.Collection;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.UpsertUserRequest;
import uk.co.mruoc.nac.entities.UserBatch;

@Builder
@Slf4j
public class UserBatchService {

  private final UserBatchFactory factory;
  private final UserBatchRepository repository;
  private final UserBatchExecutor executor;

  public UserBatch create(Collection<UpsertUserRequest> requests) {
    UserBatch batch = factory.build(requests);
    repository.create(batch);
    executor.execute(batch);
    return batch;
  }

  public UserBatch get(String id) {
    return repository.get(id).orElseThrow(() -> new UserBatchNotFoundException(id));
  }

  public Stream<UserBatch> getAll() {
    return repository.getAll();
  }

  public void deleteAll() {
    repository.deleteAll();
  }
}
