package uk.co.mruoc.nac.usecases;

import java.util.stream.Stream;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.UpsertUserRequest;
import uk.co.mruoc.nac.entities.User;

@Builder
@Slf4j
public class UserService {

  private final UserCreator creator;
  private final UserUpdater updater;
  private final UserDeleter deleter;
  private final UserFinder finder;

  public void create(UpsertUserRequest request) {
    creator.create(request);
  }

  public void update(UpsertUserRequest request) {
    updater.update(request);
  }

  public void delete(String username) {
    deleter.delete(username);
  }

  public Stream<User> getAll() {
    return finder.getAll();
  }

  public User getByUsername(String username) {
    return finder.forceGetByUsername(username);
  }
}
