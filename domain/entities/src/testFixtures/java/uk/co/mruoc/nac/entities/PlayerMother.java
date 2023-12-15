package uk.co.mruoc.nac.entities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerMother {

  public static Players players() {
    return new Players(crossesPlayer(), naughtsPlayer());
  }

  public static Player crossesPlayer() {
    return Player.builder().user(UserMother.user1()).token('X').build();
  }

  public static Player naughtsPlayer() {
    return Player.builder().user(UserMother.user2()).token('O').build();
  }
}
