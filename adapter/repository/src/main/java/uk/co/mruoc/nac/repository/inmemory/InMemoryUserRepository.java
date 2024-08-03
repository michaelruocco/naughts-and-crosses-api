package uk.co.mruoc.nac.repository.inmemory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.entities.UserPage;
import uk.co.mruoc.nac.entities.UserPageRequest;
import uk.co.mruoc.nac.usecases.UserRepository;

@Slf4j
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {

  private final Map<String, User> users;
  private final ComparatorFactory<User> comparatorFactory;

  public InMemoryUserRepository() {
    this(new HashMap<>(), new UserComparatorFactory());
  }

  @Override
  public Stream<User> getAll() {
    log.info("returning all users {}", users);
    return users.values().stream();
  }

  @Override
  public UserPage getPage(UserPageRequest request) {
    log.info("returning page of users for request {}", request);
    Collection<User> filteredUsers = toFilteredUsers(request);
    return UserPage.builder()
        .total(filteredUsers.size())
        .users(toPage(filteredUsers, request))
        .build();
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
  public void delete(String username) {
    users.remove(username);
  }

  @Override
  public boolean exists(String username) {
    return users.containsKey(username);
  }

  @Override
  public Optional<User> getByUsername(String username) {
    return Optional.ofNullable(users.get(username));
  }

  private void add(User user) {
    users.put(user.getUsername(), user);
  }

  private Collection<User> toFilteredUsers(UserPageRequest request) {
    return users.values().stream()
        .filter(new UserPageRequestPredicate(request))
        .sorted(comparatorFactory.toComparator(request.getSort()))
        .toList();
  }

  private static Collection<User> toPage(Collection<User> filteredUsers, UserPageRequest request) {
    return filteredUsers.stream().skip(request.getOffset()).limit(request.getLimit()).toList();
  }
}
