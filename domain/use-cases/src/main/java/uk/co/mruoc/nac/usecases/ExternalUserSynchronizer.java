package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import lombok.Builder;
import uk.co.mruoc.nac.entities.User;

@Builder
public class ExternalUserSynchronizer {

  private final ExternalUserService externalUserService;
  private final UserRepository repository;

  public void synchronize() {
    externalUserService.getAllUsers().forEach(repository::upsert);
    repository.getAll().forEach(this::deleteIfNotPresentInExternalUsers);
  }

  private void deleteIfNotPresentInExternalUsers(User dbUser) {
    Optional<User> externalUser = externalUserService.getByUsername(dbUser.getUsername());
    if (externalUser.isEmpty()) {
      repository.delete(dbUser.getId());
    }
  }
}
