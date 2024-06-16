package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import java.util.stream.Stream;
import uk.co.mruoc.nac.entities.User;

public interface UserRepository {

  default void upsert(User user) {
    if (getByUsername(user.getUsername()).isPresent()) {
      update(user);
    } else {
      create(user);
    }
  }

  Optional<User> getByUsername(String username);

  Stream<User> getAll();

  void create(User user);

  void update(User user);

  void delete(String username);
}
