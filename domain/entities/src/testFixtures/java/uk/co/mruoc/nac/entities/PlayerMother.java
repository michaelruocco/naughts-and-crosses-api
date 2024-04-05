package uk.co.mruoc.nac.entities;

import java.util.Arrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerMother {

  public static Players empty() {
    return new Players();
  }

  public static Players withTokens(Character... tokens) {
    return new Players(Arrays.stream(tokens).map(PlayerMother::withToken).toList());
  }

  public static Players players() {
    return new Players(crossesPlayer(), naughtsPlayer());
  }

  public static Player crossesPlayer() {
    return Player.builder().user(UserMother.user1()).token('X').build();
  }

  public static Player naughtsPlayer() {
    return Player.builder().user(UserMother.user2()).token('O').build();
  }

  public static Player withToken(char token) {
    return Player.builder().user(UserMother.user1()).token(token).build();
  }
}
