package uk.co.mruoc.nac.repository.inmemory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.usecases.UserRepository;

@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {

  private final Map<String, User> users;

  public InMemoryUserRepository() {
    this(new HashMap<>());
  }

  @Override
  public Stream<User> getAll() {
    return users.values().stream();
  }

  @Override
  public void create(User user) {
    add(user);
  }

  @Override
  public void update(User user) {
    add(user);
  }

  @Override
  public void deleteAll() {
    users.clear();
  }

  @Override
  public void delete(String username) {
    users.remove(username);
  }

  @Override
  public Optional<User> getByUsername(String username) {
    return Optional.ofNullable(users.get(username));
  }

  private void add(User user) {
    users.put(user.getUsername(), user);
  }
}
