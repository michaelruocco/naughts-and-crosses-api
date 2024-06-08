package uk.co.mruoc.nac.usecases;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.UpdateUserRequest;
import uk.co.mruoc.nac.entities.User;

@Builder
@Slf4j
public class UserUpdater {

  private final ExternalUserService externalService;
  private final UserRepository repository;

  public void update(UpdateUserRequest request) {
    String id = request.getId();
    User existingUser =
        externalService.getById(id).orElseThrow(() -> new UserNotFoundByIdException(id));
    User updatedUser = existingUser.update(request);
    externalService.update(updatedUser);
    repository.update(updatedUser);
  }
}
