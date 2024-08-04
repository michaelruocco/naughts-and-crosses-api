package uk.co.mruoc.nac.entities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserNameMother {

  public static UserName admin() {
    return UserName.builder().first("Admin").last("User").build().tryToPopulateAll();
  }

  public static UserName user1() {
    return UserName.builder().full("User One").build().tryToPopulateAll();
  }

  public static UserName user2() {
    return UserName.builder().first("User").last("Two").build().tryToPopulateAll();
  }

  public static UserName empty() {
    return UserName.builder().build();
  }
}
