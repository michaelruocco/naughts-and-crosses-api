package uk.co.mruoc.nac.entities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsersCollector {

  public static GenericCollector<User, Users> usersCollector() {
    return new GenericCollector<>(Users::new);
  }
}
