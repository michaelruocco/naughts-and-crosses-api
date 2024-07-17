package uk.co.mruoc.nac.usecases;

import java.util.stream.Stream;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.UpsertUserRequest;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.entities.UserPage;
import uk.co.mruoc.nac.entities.UserPageRequest;

@Builder
@Slf4j
public class UserService {

  private final AuthenticatedUserValidator userValidator;
  private final UserCreator creator;
  private final UserUpdater updater;
  private final UserDeleter deleter;
  private final UserFinder finder;

  public void create(UpsertUserRequest request) {
    userValidator.validateIsAdmin();
    creator.create(request);
  }

  public void update(UpsertUserRequest request) {
    userValidator.validateIsAdmin();
    updater.update(request);
  }

  public void delete(String username) {
    userValidator.validateIsAdmin();
    deleter.delete(username);
  }

  public Stream<User> getAll() {
    return finder.getAll();
  }

  public UserPage createPage(UserPageRequest request) {
    return finder.getPage(request);
  }

  public User getByUsername(String username) {
    return finder.forceGetByUsername(username);
  }
}
