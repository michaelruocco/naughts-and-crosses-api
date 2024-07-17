package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.entities.UserPage;
import uk.co.mruoc.nac.entities.UserPageRequest;

@RequiredArgsConstructor
@Slf4j
public class UserFinder {

  private final UserRepository repository;

  public Stream<User> getAll() {
    return repository.getAll();
  }

  public UserPage getPage(UserPageRequest request) {
    return repository.getPage(request);
  }

  public User forceGetByUsername(String username) {
    return getByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
  }

  public Optional<User> getByUsername(String username) {
    return repository.getByUsername(username);
  }
}
