package uk.co.mruoc.nac.usecases;

import uk.co.mruoc.nac.entities.Player;
import uk.co.mruoc.nac.entities.Players;
import uk.co.mruoc.nac.entities.User;

public class PlayerFactory {

  public Players build() {
    return new Players(toCrossesPlayer(buildCrossesUser()), toNaughtsPlayer(buildNaughtsUser()));
  }

  private static Player toCrossesPlayer(User user) {
    return Player.builder().user(user).token('X').build();
  }

  private static Player toNaughtsPlayer(User user) {
    return Player.builder().user(user).token('O').build();
  }

  private static User buildCrossesUser() {
    return User.builder().id("1").name("Player One").email("player-1@email.com").build();
  }

  private static User buildNaughtsUser() {
    return User.builder().id("2").name("Player Two").email("player-2@email.com").build();
  }
}
