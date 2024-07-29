package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import java.util.stream.Stream;
import uk.co.mruoc.nac.entities.UpsertUserRequest;
import uk.co.mruoc.nac.entities.User;

public interface ExternalUserService {

  User create(UpsertUserRequest request);

  void update(User user);

  void delete(String id);

  Stream<User> getAllUsers();

  Optional<User> getByUsername(String username);

  Stream<String> getAllGroups();
}
