package uk.co.mruoc.nac.usecases;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.UserBatch;

@Builder
@Slf4j
public class UserBatchExecutor {

  private final UserCreator creator;
  private final UserBatchRepository repository;

  public void execute(UserBatch batch) {
    Runnable runnable =
        UserBatchRunnable.builder().creator(creator).repository(repository).batch(batch).build();
    Thread thread = new Thread(runnable);
    thread.start();
    log.info("executing thread {} for user batch {}", thread.getName(), batch.getId());
  }
}
