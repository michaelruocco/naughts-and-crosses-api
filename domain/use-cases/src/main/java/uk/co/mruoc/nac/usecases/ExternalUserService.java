package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import java.util.stream.Stream;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.User;

public interface ExternalUserService {

  User create(CreateUserRequest request);

  void update(User user);

  void delete(String id);

  Stream<User> getAll();

  Optional<User> getByUsername(String id);
}
