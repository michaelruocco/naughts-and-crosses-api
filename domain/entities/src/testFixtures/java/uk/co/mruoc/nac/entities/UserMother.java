package uk.co.mruoc.nac.entities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMother {

  public static User user1() {
    return User.builder()
        .id("user-1")
        .email("user-1@email.com")
        .firstName("User")
        .lastName("One")
        .build();
  }

  public static User user2() {
    return User.builder()
        .id("user-2")
        .email("user-2@email.com")
        .firstName("User")
        .lastName("Two")
        .build();
  }
}
