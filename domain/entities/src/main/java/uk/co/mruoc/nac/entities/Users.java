package uk.co.mruoc.nac.entities;

import static uk.co.mruoc.nac.entities.UsersCollector.usersCollector;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
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

  public Users sortById() {
    return values.stream().sorted(new UserIdComparator()).collect(usersCollector());
  }

  public Users add(User user) {
    return new Users(Stream.concat(values.stream(), Stream.of(user)));
  }

  public Optional<User> findById(String id) {
    return values.stream().filter(user -> user.hasId(id)).findFirst();
  }
}
