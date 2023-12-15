package uk.co.mruoc.nac.repository.postgres.converter;

import java.util.Collection;
import java.util.List;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.repository.postgres.dto.DbUser;

public class DbUserConverter {

  public Collection<DbUser> toDbUsers(Collection<User> users) {
    return users.stream().map(this::toDbUser).toList();
  }

  public DbUser toDbUser(User user) {
    return DbUser.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).build();
  }

  public List<User> toUsers(Collection<DbUser> dbUsers) {
    return dbUsers.stream().map(this::toUser).toList();
  }

  public User toUser(DbUser dbUser) {
    return User.builder()
        .id(dbUser.getId())
        .name(dbUser.getName())
        .email(dbUser.getEmail())
        .build();
  }
}
