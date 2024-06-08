package uk.co.mruoc.nac.repository.postgres.converter;

import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.repository.postgres.dto.DbUser;

public class DbUserConverter {

  public DbUser toDbUser(User user) {
    return DbUser.builder()
        .id(user.getId())
        .username(user.getUsername())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .emailVerified(user.isEmailVerified())
        .build();
  }

  public User toUser(DbUser dbUser) {
    return User.builder()
        .id(dbUser.getId())
        .username(dbUser.getUsername())
        .firstName(dbUser.getFirstName())
        .lastName(dbUser.getLastName())
        .email(dbUser.getEmail())
        .emailVerified(dbUser.isEmailVerified())
        .build();
  }
}
