package uk.co.mruoc.nac.usecases;

import java.time.Clock;
import java.time.Instant;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.UserBatch;
import uk.co.mruoc.nac.entities.UserBatchError;

@Builder
@Slf4j
public class UserBatchRunnable implements Runnable {

  private final UserBatch batch;
  private final UserCreator creator;
  private final UserBatchRepository repository;
  private final Clock clock;

  @Override
  public void run() {
    UserBatch updatedBatch = batch;
    for (CreateUserRequest request : updatedBatch.getRequests()) {
      updatedBatch = updateBatch(updatedBatch, request);
      repository.update(updatedBatch);
    }
  }

  private UserBatch updateBatch(UserBatch userBatch, CreateUserRequest request) {
    Instant now = clock.instant();
    try {
      return userBatch.addUser(creator.create(request), now);
    } catch (Exception e) {
      log.warn(e.getMessage(), e);
      return userBatch.addError(toError(request, e), now);
    }
  }

  private static UserBatchError toError(CreateUserRequest request, Exception e) {
    return UserBatchError.builder().username(request.getUsername()).message(e.getMessage()).build();
  }
}
