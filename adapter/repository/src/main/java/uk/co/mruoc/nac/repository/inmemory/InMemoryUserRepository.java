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
  public void delete(String id) {
    users.remove(id);
  }

  @Override
  public Optional<User> get(String id) {
    return Optional.ofNullable(users.get(id));
  }

  private void add(User user) {
    users.put(user.getId(), user);
  }
}
