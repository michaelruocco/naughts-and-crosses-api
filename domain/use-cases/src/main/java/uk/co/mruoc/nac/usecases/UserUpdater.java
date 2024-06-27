package uk.co.mruoc.nac.usecases;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.UpsertUserRequest;
import uk.co.mruoc.nac.entities.User;

@Builder
@Slf4j
public class UserUpdater {

  private final ExternalUserService externalService;
  private final UserRepository repository;

  public void update(UpsertUserRequest request) {
    String username = request.getUsername();
    User existingUser =
        externalService
            .getByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));
    User updatedUser = existingUser.update(request);
    externalService.update(updatedUser);
    repository.update(updatedUser);
  }
}
