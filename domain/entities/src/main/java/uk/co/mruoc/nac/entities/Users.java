package uk.co.mruoc.nac.entities;

import static uk.co.mruoc.nac.entities.UsersCollector.usersCollector;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Users {

  private final Collection<User> values;

  public Stream<User> stream() {
    return values.stream();
  }

  public Users sortByUsername() {
    return sort(Comparator.comparing(User::getUsername));
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
