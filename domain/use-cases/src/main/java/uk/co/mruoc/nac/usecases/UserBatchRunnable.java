package uk.co.mruoc.nac.usecases;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.UserBatch;

@Builder
@Slf4j
public class UserBatchRunnable implements Runnable {

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
