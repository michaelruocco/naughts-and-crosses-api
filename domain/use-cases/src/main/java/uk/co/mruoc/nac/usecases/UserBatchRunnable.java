package uk.co.mruoc.nac.usecases;

import java.time.Clock;
import java.time.Instant;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.UpsertUserRequest;
import uk.co.mruoc.nac.entities.UserBatch;
import uk.co.mruoc.nac.entities.UserBatchError;

@Builder
@Slf4j
public class UserBatchRunnable implements Runnable {

  private final UserBatch batch;
  private final UserUpserter upserter;
  private final UserBatchRepository repository;
  private final Clock clock;

  @Override
  public void run() {
    UserBatch updatedBatch = batch;
    for (UpsertUserRequest request : updatedBatch.getRequests()) {
      updatedBatch = updateBatch(updatedBatch, request);
      repository.update(updatedBatch);
    }
  }

  private UserBatch updateBatch(UserBatch userBatch, UpsertUserRequest request) {
    Instant now = clock.instant();
    try {
      return userBatch.addUser(upserter.upsert(request), now);
    } catch (Exception e) {
      log.warn(e.getMessage(), e);
      return userBatch.addError(toError(request, e), now);
    }
  }

  private static UserBatchError toError(UpsertUserRequest request, Exception e) {
    return UserBatchError.builder().username(request.getUsername()).message(e.getMessage()).build();
  }
}
