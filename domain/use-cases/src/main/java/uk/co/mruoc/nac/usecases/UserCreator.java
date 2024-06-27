package uk.co.mruoc.nac.usecases;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.UpsertUserRequest;
import uk.co.mruoc.nac.entities.User;

@Builder
@Slf4j
public class UserCreator {

  private final ExternalUserService externalService;
  private final UserRepository repository;

  public User create(UpsertUserRequest request) {
    User user = externalService.create(request);
    repository.create(user);
    return user;
  }
}
