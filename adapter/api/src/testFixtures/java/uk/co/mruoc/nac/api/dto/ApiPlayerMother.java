package uk.co.mruoc.nac.api.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiPlayerMother {

  public static ApiPlayer buildMinimalCrossesPlayer() {
    return buildMinimalPlayer("user-1", "User One", 'X');
  }

  public static ApiPlayer buildMinimalNaughtsPlayer() {
    return buildMinimalPlayer("user-2", "User Two", 'O');
  }

  private static ApiPlayer buildMinimalPlayer(String username, String fullName, char token) {
    return ApiPlayer.builder()
        .user(ApiUser.builder().username(username).fullName(fullName).build())
        .token(token)
        .build();
  }
}
