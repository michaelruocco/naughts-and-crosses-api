package uk.co.mruoc.nac.usecases;

import static uk.co.mruoc.nac.entities.UsersCollector.usersCollector;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.UpdateUserRequest;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.entities.Users;

@Builder
@Slf4j
public class UserService {

  private final UserCreator creator;
  private final UserUpdater updater;
  private final UserDeleter deleter;
  private final UserRepository repository;

  public void create(CreateUserRequest request) {
    creator.create(request);
  }

  public void update(UpdateUserRequest request) {
    updater.update(request);
  }

  public void delete(String username) {
    deleter.delete(username);
  }

  public Users getAll() {
    return repository.getAll().collect(usersCollector()).sortByUsername();
  }

  public User getByUsername(String username) {
    return repository
        .getByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(username));
  }
}
