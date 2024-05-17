package uk.co.mruoc.nac.user.inmemory;

import java.util.Collection;
import java.util.List;
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

@RequiredArgsConstructor
public class StubUserService implements ExternalUserService {

  private final Map<String, User> users;
  private final Supplier<UUID> idSupplier;

  public StubUserService() {
    this(buildUsers(), UUID::randomUUID);
  }

  @Override
  public Stream<User> getAll() {
    return users.values().stream().sorted(new UserComparator());
  }

  @Override
  public void create(Collection<CreateUserRequest> requests) {
    requests.stream().map(this::toUser).forEach(this::add);
  }

  @Override
  public Optional<User> get(String id) {
    return Optional.ofNullable(users.get(id));
  }

  private void add(User user) {
    users.put(user.getId(), user);
  }

  private User toUser(CreateUserRequest request) {
    return User.builder()
        .id(idSupplier.get().toString())
        .username(request.getUsername())
        .email(request.getEmail())
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .build();
  }

  private static Map<String, User> buildUsers() {
    return toMap(List.of(user1(), user2()));
  }

  private static Map<String, User> toMap(Collection<User> userCollection) {
    return userCollection.stream().collect(Collectors.toMap(User::getId, Function.identity()));
  }

  private static User user1() {
    return User.builder()
        .id("707d9fa6-13dd-4985-93aa-a28f01e89a6b")
        .username("user-1")
        .email("user-1@email.com")
        .firstName("User")
        .lastName("One")
        .build();
  }

  private static User user2() {
    return User.builder()
        .id("dadfde25-9924-4982-802d-dfd0bce2218d")
        .username("user-2")
        .email("user-2@email.com")
        .firstName("User")
        .lastName("Two")
        .build();
  }
}
