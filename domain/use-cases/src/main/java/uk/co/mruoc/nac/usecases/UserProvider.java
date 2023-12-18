package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import java.util.stream.Stream;

import uk.co.mruoc.nac.entities.User;

public interface UserProvider {

  Optional<User> get(String id);

  Stream<User> getAll();
}
