package uk.co.mruoc.nac.repository.postgres.converter;

import java.util.Collection;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.repository.postgres.dto.DbUser;

public class DbUserConverter {

  public Collection<DbUser> toDbUsers(Collection<User> users) {
    return users.stream().map(this::toDbUser).toList();
  }

  public DbUser toDbUser(User user) {
    return DbUser.builder()
        .id(user.getId())
        .username(user.getUsername())
        .name(user.getName())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .emailVerified(user.isEmailVerified())
        .status(user.getStatus())
        .groups(user.getGroups())
        .build();
  }

  public Collection<User> toUsers(Collection<DbUser> dbUsers) {
    return dbUsers.stream().map(this::toUser).toList();
  }

  public User toUser(DbUser dbUser) {
    return User.builder()
        .id(dbUser.getId())
        .username(dbUser.getUsername())
        .name(dbUser.getName())
        .firstName(dbUser.getFirstName())
        .lastName(dbUser.getLastName())
        .email(dbUser.getEmail())
        .emailVerified(dbUser.isEmailVerified())
        .status(dbUser.getStatus())
        .groups(dbUser.getGroups())
        .build();
  }
}
