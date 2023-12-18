package uk.co.mruoc.nac.usecases;

import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.User;

@RequiredArgsConstructor
public class UserService {

  private final UserProvider provider;

  public Stream<User> getAll() {
    return provider.getAll();
  }

  public User get(String id) {
    return provider.get(id).orElseThrow(() -> new UserNotFoundException(id));
  }
}
