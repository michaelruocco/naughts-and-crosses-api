package uk.co.mruoc.nac.user.inmemory;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.usecases.UserNotFoundException;
import uk.co.mruoc.nac.usecases.UserProvider;
import uk.co.mruoc.nac.usecases.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class StubUserProvider implements UserProvider {

  private final Map<String, User> users;

  public StubUserProvider() {
    this(buildUsers());
  }

  @Override
  public Stream<User> getAll() {
    return users.values().stream().sorted(new UserComparator());
  }

  @Override
  public Optional<User> get(String id) {
    return Optional.ofNullable(users.get(id));
  }

  private static Map<String, User> buildUsers() {
    return toMap(List.of(user1(), user2()));
  }

  private static Map<String, User> toMap(Collection<User> userCollection) {
    return userCollection.stream().collect(Collectors.toMap(User::getId, Function.identity()));
  }

  private static User user1() {
    return User.builder().id("user-1").email("user-1@email.com").name("User One").build();
  }

  private static User user2() {
    return User.builder().id("user-2").email("user-2@email.com").name("User Two").build();
  }
}
