package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.User;

@RequiredArgsConstructor
@Slf4j
public class UserFinder {

  private final UserRepository repository;

  public Stream<User> getAll() {
    return repository.getAll();
  }

  public User forceGetByUsername(String username) {
    return getByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
  }

  public Optional<User> getByUsername(String username) {
    return repository.getByUsername(username);
  }
}
