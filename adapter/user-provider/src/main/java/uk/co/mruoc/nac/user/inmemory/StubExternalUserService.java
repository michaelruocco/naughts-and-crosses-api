package uk.co.mruoc.nac.user.inmemory;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.entities.Users;
import uk.co.mruoc.nac.usecases.ExternalUserService;
import uk.co.mruoc.nac.usecases.UsernameAlreadyExistsException;

@RequiredArgsConstructor
public class StubExternalUserService implements ExternalUserService {

  private final Users users;
  private final Supplier<UUID> idSupplier;

  public StubExternalUserService() {
    this(buildUsers(), UUID::randomUUID);
  }

  @Override
  public Stream<User> getAll() {
    return users.stream();
  }

  @Override
  public Optional<User> getById(String id) {
    return users.findById(id);
  }

  @Override
  public User create(CreateUserRequest request) {
    User user = toUser(request);
    add(user);
    return user;
  }

  private void add(User user) {
    String username = user.getUsername();
    if (users.containsUserWithUsername(username)) {
      throw new UsernameAlreadyExistsException(username);
    }
    users.add(user);
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

  private static Users buildUsers() {
    return new Users(user1(), user2());
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
