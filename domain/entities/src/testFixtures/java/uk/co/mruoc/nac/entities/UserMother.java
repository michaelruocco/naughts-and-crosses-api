package uk.co.mruoc.nac.entities;

import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMother {

  private static final String PLAYER_GROUP = "player";
  private static final String CONFIRMED_STATUS = "CONFIRMED";

  public static User admin() {
    return adminBuilder().build();
  }

  public static User user1() {
    return User.builder()
        .username("user-1")
        .id("id-1")
        .name(UserNameMother.user1())
        .email("user-1@email.com")
        .emailVerified(true)
        .groups(Set.of(PLAYER_GROUP))
        .status(CONFIRMED_STATUS)
        .build();
  }

  public static User user2() {
    return User.builder()
        .username("user-2")
        .id("id-2")
        .name(UserNameMother.user2())
        .email("user-2@email.com")
        .emailVerified(true)
        .groups(Set.of(PLAYER_GROUP))
        .status(CONFIRMED_STATUS)
        .build();
  }

  public static User.UserBuilder adminBuilder() {
    return User.builder()
        .username("admin")
        .id("id-0")
        .name(UserNameMother.admin())
        .email("admin-user@email.com")
        .emailVerified(true)
        .groups(Set.of("admin"))
        .status(CONFIRMED_STATUS);
  }
}
