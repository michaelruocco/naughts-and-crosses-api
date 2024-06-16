package uk.co.mruoc.nac.usecases;

import java.util.Collection;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.UserBatch;

@Builder
@Slf4j
public class UserBatchService {

  private final UserBatchFactory factory;
  private final UserBatchRepository repository;
  private final UserBatchExecutor executor;

  public UserBatch create(Collection<CreateUserRequest> requests) {
    UserBatch batch = factory.build(requests);
    repository.create(batch);
    executor.execute(batch);
    return batch;
  }

  public UserBatch get(String id) {
    return repository.get(id).orElseThrow(() -> new UserBatchNotFoundException(id));
  }
}
