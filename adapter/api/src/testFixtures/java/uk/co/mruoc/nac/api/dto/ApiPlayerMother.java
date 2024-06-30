package uk.co.mruoc.nac.api.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiPlayerMother {

  public static ApiPlayer buildMinimalCrossesPlayer() {
    return buildMinimalPlayer("user-1", 'X');
  }

  public static ApiPlayer buildMinimalNaughtsPlayer() {
    return buildMinimalPlayer("user-2", 'O');
  }

  public static ApiPlayer buildMinimalPlayer(String username, char token) {
    return ApiPlayer.builder()
        .user(ApiUser.builder().username(username).build())
        .token(token)
        .build();
  }
}
