package uk.co.mruoc.nac.usecases;

import java.time.Clock;
import java.util.concurrent.Executor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.UserBatch;

@Builder
@Slf4j
public class UserBatchExecutor {

  private final UserUpserter upserter;
  private final UserBatchRepository repository;
  private final Clock clock;
  private final Executor executor;

  public void execute(UserBatch batch) {
    Runnable runnable =
        UserBatchRunnable.builder()
            .upserter(upserter)
            .repository(repository)
            .clock(clock)
            .batch(batch)
            .build();
    executor.execute(runnable);
  }
}
