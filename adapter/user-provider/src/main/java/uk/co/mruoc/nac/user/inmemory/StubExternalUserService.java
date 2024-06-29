package uk.co.mruoc.nac.user.inmemory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.UpsertUserRequest;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.usecases.ExternalUserService;
import uk.co.mruoc.nac.usecases.UserAlreadyExistsException;
import uk.co.mruoc.nac.usecases.UserNotFoundException;

@RequiredArgsConstructor
public class StubExternalUserService implements ExternalUserService {

  private static final String PLAYER_GROUP = "player";
  private static final String CONFIRMED_STATUS = "CONFIRMED";

  private final Map<String, User> users;
  private final Supplier<UUID> uuidSupplier;

  public StubExternalUserService(Supplier<UUID> uuidSupplier) {
    this(buildUsers(), uuidSupplier);
  }

  @Override
  public User create(UpsertUserRequest request) {
    User user = toUser(request);
    String username = user.getUsername();
    if (users.containsKey(username)) {
      throw new UserAlreadyExistsException(username);
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
  public Stream<User> getAllUsers() {
    return users.values().stream();
  }

  @Override
  public Optional<User> getByUsername(String username) {
    return Optional.ofNullable(users.get(username));
  }

  @Override
  public Collection<String> getAllGroups() {
    return Set.of("admin", PLAYER_GROUP);
  }

  private User toUser(UpsertUserRequest request) {
    return User.builder()
        .id(uuidSupplier.get().toString())
        .username(request.getUsername())
        .email(request.getEmail())
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .emailVerified(request.isEmailVerified())
        .status("FORCE_CHANGE_PASSWORD")
        .groups(request.getGroups())
        .build();
  }

  private static Map<String, User> buildUsers() {
    return Stream.of(admin(), user1(), user2())
        .collect(Collectors.toMap(User::getUsername, Function.identity()));
  }

  private static User admin() {
    return User.builder()
        .username("admin")
        .id("42bfdad9-c66a-4d5a-aa48-a97d6d3574af")
        .firstName("Admin")
        .lastName("User")
        .email("admin@email.com")
        .emailVerified(true)
        .status(CONFIRMED_STATUS)
        .groups(Set.of("admin"))
        .build();
  }

  private static User user1() {
    return User.builder()
        .username("user-1")
        .id("707d9fa6-13dd-4985-93aa-a28f01e89a6b")
        .firstName("User")
        .lastName("One")
        .email("user-1@email.com")
        .emailVerified(true)
        .status(CONFIRMED_STATUS)
        .groups(Set.of(PLAYER_GROUP))
        .build();
  }

  private static User user2() {
    return User.builder()
        .username("user-2")
        .id("dadfde25-9924-4982-802d-dfd0bce2218d")
        .firstName("User")
        .lastName("Two")
        .email("user-2@email.com")
        .emailVerified(true)
        .status(CONFIRMED_STATUS)
        .groups(Set.of(PLAYER_GROUP))
        .build();
  }
}
