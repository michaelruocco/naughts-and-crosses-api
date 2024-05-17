package uk.co.mruoc.nac.usecases;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.User;

public interface ExternalUserService {

  Optional<User> get(String id);

  Stream<User> getAll();

  void create(Collection<CreateUserRequest> requests);
}
