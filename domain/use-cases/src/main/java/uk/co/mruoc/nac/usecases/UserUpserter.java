package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.UpsertUserRequest;
import uk.co.mruoc.nac.entities.User;

@Builder
@Slf4j
public class UserUpserter {

  private final UserFinder finder;
  private final UserCreator creator;
  private final UserUpdater updater;

  public User upsert(UpsertUserRequest request) {
    String username = request.getUsername();
    log.info("upserting user {}", username);
    Optional<User> user = finder.getByUsername(username);
    if (user.isPresent()) {
      updater.update(request);
      return finder.forceGetByUsername(username);
    }
    return creator.create(request);
  }
}
