package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.User;

@Builder
@Slf4j
public class UserDeleter {

  private final ExternalUserService externalService;
  private final UserRepository repository;

  public void delete(String username) {
    Optional<User> existingUser = externalService.getByUsername(username);
    if (existingUser.isEmpty()) {
      log.info("user with username {} does not exist, delete not required", username);
      return;
    }
    externalService.delete(username);
    repository.delete(username);
  }
}
