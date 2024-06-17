package uk.co.mruoc.nac.user.inmemory;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.usecases.ExternalUserService;
import uk.co.mruoc.nac.usecases.UserNotFoundException;
import uk.co.mruoc.nac.usecases.UsernameAlreadyExistsException;

@RequiredArgsConstructor
public class StubExternalUserService implements ExternalUserService {

  private final Map<String, User> users;
  private final Supplier<UUID> uuidSupplier;

  public StubExternalUserService(Supplier<UUID> uuidSupplier) {
    this(buildUsers(), uuidSupplier);
  }

  @Override
  public User create(CreateUserRequest request) {
    User user = toUser(request);
    String username = user.getUsername();
    if (users.containsKey(username)) {
      throw new UsernameAlreadyExistsException(username);
    }
    users.put(username, user);
    return user;
  }

  @Override
  public void update(User user) {
    String username = user.getUsername();
    if (!users.containsKey(username)) {
      throw new UserNotFoundException(username);
    }
    users.put(username, user);
  }

  @Override
  public void delete(String username) {
    users.remove(username);
  }

  @Override
  public Stream<User> getAll() {
    return users.values().stream();
  }

  @Override
  public Optional<User> getByUsername(String username) {
    return Optional.ofNullable(users.get(username));
  }

  private User toUser(CreateUserRequest request) {
    return User.builder()
        .id(uuidSupplier.get().toString())
        .username(request.getUsername())
        .email(request.getEmail())
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .emailVerified(request.isEmailVerified())
        .build();
  }

  private static Map<String, User> buildUsers() {
    return Stream.of(user1(), user2())
        .collect(Collectors.toMap(User::getUsername, Function.identity()));
  }

  private static User user1() {
    return User.builder()
        .id("707d9fa6-13dd-4985-93aa-a28f01e89a6b")
        .username("user-1")
        .email("user-1@email.com")
        .emailVerified(true)
        .firstName("User")
        .lastName("One")
        .build();
  }

  private static User user2() {
    return User.builder()
        .id("dadfde25-9924-4982-802d-dfd0bce2218d")
        .username("user-2")
        .email("user-2@email.com")
        .emailVerified(true)
        .firstName("User")
        .lastName("Two")
        .build();
  }
}
