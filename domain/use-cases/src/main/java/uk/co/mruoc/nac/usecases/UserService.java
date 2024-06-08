package uk.co.mruoc.nac.usecases;

import static uk.co.mruoc.nac.entities.UsersCollector.usersCollector;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.entities.Users;

@Builder
@Slf4j
public class UserService {

  private final UserCreator creator;
  private final UserRepository repository;

  public void create(CreateUserRequest request) {
    creator.create(request);
  }

  public Users getAll() {
    return repository.getAll().collect(usersCollector()).sortById();
  }

  public User get(String id) {
    return repository.get(id).orElseThrow(() -> new UserNotFoundException(id));
  }
}
