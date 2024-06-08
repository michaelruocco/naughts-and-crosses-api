package uk.co.mruoc.nac.entities;

import static uk.co.mruoc.nac.entities.UsersCollector.usersCollector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Users {

  private final Collection<User> values;

  public Users(User... users) {
    this(List.of(users));
  }

  public Users(Stream<User> stream) {
    this(stream.toList());
  }

  public Stream<User> stream() {
    return values.stream();
  }

  public int size() {
    return values.size();
  }

  public boolean containsUserWithUsername(String username) {
    return values.stream().anyMatch(user -> user.hasUsername(username));
  }

  public Users sortByUsername() {
    return sort(Comparator.comparing(User::getUsername));
  }

  public Users add(User user) {
    if (containsUserWithId(user.getId())) {
      throw new UserWithIdAlreadyExistsException(user.getId());
    }
    return new Users(Stream.concat(values.stream(), Stream.of(user)));
  }

  public Users update(User user) {
    Optional<User> existingUser = findById(user.getId());
    if (existingUser.isEmpty()) {
      throw new UserNotFoundByIdException(user.getId());
    }
    Collection<User> updated = new ArrayList<>(values);
    updated.remove(existingUser.get());
    return new Users(Stream.concat(updated.stream(), Stream.of(user)));
  }

  public boolean containsUserWithId(String id) {
    return findById(id).isPresent();
  }

  public Optional<User> findById(String id) {
    return find(user -> user.hasId(id));
  }

  public Optional<User> findByUsername(String username) {
    return find(user -> user.hasUsername(username));
  }

  private Users sort(Comparator<User> comparator) {
    return values.stream().sorted(comparator).collect(usersCollector());
  }

  private Optional<User> find(Predicate<User> predicate) {
    return values.stream().filter(predicate).findFirst();
  }
}
