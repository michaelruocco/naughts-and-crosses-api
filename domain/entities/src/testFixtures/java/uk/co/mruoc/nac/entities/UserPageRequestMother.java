package uk.co.mruoc.nac.entities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserPageRequestMother {

  public static UserPageRequest build() {
    return UserPageRequest.builder().limit(10).offset(0).build();
  }
}
