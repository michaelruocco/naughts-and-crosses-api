package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import lombok.Builder;
import uk.co.mruoc.nac.entities.User;

@Builder
public class ExternalUserSynchronizer {

  private final AuthenticatedUserValidator userValidator;
  private final ExternalUserService externalUserService;
  private final UserRepository repository;

  public void adminOnlySynchronize(String username) {
    userValidator.validateIsAdmin();
    synchronize(username);
  }

  public void adminOnlySynchronize() {
    userValidator.validateIsAdmin();
    synchronize();
  }

  public void synchronizeIfNotPresent(String username) {
    if (!repository.exists(username)) {
      synchronize(username);
    }
  }

  public void synchronize(String username) {
    externalUserService
        .getByUsername(username)
        .ifPresentOrElse(repository::upsert, () -> repository.delete(username));
  }

  public void synchronize() {
    externalUserService.getAllUsers().forEach(repository::upsert);
    repository.getAll().forEach(this::deleteIfNotPresentInExternalUsers);
  }

  private void deleteIfNotPresentInExternalUsers(User dbUser) {
    Optional<User> externalUser = externalUserService.getByUsername(dbUser.getUsername());
    if (externalUser.isEmpty()) {
      repository.delete(dbUser.getUsername());
    }
  }
}
