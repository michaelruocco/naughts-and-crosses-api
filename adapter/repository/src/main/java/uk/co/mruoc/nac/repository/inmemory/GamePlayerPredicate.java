package uk.co.mruoc.nac.repository.inmemory;

import java.util.Objects;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.Game;

@RequiredArgsConstructor
public class GamePlayerPredicate implements Predicate<Game> {

  private final String username;

  @Override
  public boolean test(Game game) {
    if (Objects.isNull(username)) {
      return true;
    }
    return game.getPlayers().containsUsername(username);
  }
}
