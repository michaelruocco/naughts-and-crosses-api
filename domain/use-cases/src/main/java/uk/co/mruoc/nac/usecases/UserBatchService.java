package uk.co.mruoc.nac.usecases;

import java.util.Collection;
import java.util.UUID;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.UserBatch;

@Builder
@Slf4j
public class UserBatchService {

  private final UserBatchRepository repository;
  private final UserCreator creator;

  public UserBatch create(Collection<CreateUserRequest> requests) {
    UserBatch batch = new UserBatch(UUID.randomUUID().toString(), requests);
    repository.save(batch);
    return batch;
  }

  public void execute(UserBatch batch) {
    Runnable runnable = UserBatchRunnable.builder().batch(batch).creator(creator).build();
    Thread thread = new Thread(runnable);
    thread.start();
    log.info("executing thread {} for user batch {}", thread.getId(), batch.getId());
  }

  public UserBatch get(String id) {
    return repository.get(id).orElseThrow(() -> new UserBatchNotFoundException(id));
  }

  @Builder
  private static class UserBatchRunnable implements Runnable {

    private final UserBatch batch;
    private final UserCreator creator;

    @Override
    public void run() {
      UserBatch updatedBatch = batch;
      for (CreateUserRequest request : updatedBatch.getRequests()) {
        updatedBatch = updateBatch(updatedBatch, request);
      }
    }

    private UserBatch updateBatch(UserBatch userBatch, CreateUserRequest request) {
      try {
        return userBatch.addUser(creator.create(request));
      } catch (Exception e) {
        log.warn(e.getMessage(), e);
        return userBatch.addError(request.getUsername(), e);
      }
    }
  }
}
