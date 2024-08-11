package uk.co.mruoc.nac.repository.inmemory;

import java.util.Objects;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.Game;

@RequiredArgsConstructor
public class GameCompletePredicate implements Predicate<Game> {

  private final Boolean complete;

  @Override
  public boolean test(Game game) {
    if (Objects.isNull(complete)) {
      return true;
    }
    return complete == game.isComplete();
  }
}
