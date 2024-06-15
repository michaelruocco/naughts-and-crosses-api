package uk.co.mruoc.nac.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.UserBatch;

@RequiredArgsConstructor
@Slf4j
public class UserBatchExecutor {

  private final UserCreator creator;

  public void execute(UserBatch batch) {
    Runnable runnable = UserBatchRunnable.builder().batch(batch).creator(creator).build();
    Thread thread = new Thread(runnable);
    thread.start();
    log.info("executing thread {} for user batch {}", thread.getName(), batch.getId());
  }
}
