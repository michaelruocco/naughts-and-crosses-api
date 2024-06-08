package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import java.util.stream.Stream;
import uk.co.mruoc.nac.entities.User;

public interface UserRepository {

  default void upsert(User user) {
    if (getById(user.getId()).isPresent()) {
      update(user);
    } else {
      create(user);
    }
  }

  Optional<User> getByUsername(String username);

  Optional<User> getById(String id);

  Stream<User> getAll();

  void create(User user);

  void update(User user);

  void deleteAll();

  void delete(String id);
}
