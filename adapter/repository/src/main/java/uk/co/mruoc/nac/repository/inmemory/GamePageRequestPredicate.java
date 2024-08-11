package uk.co.mruoc.nac.repository.inmemory;

import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.GamePageRequest;

@RequiredArgsConstructor
public class GamePageRequestPredicate implements Predicate<Game> {

  private final GamePageRequest request;

  @Override
  public boolean test(Game game) {
    return request.getComplete().map(complete -> complete == game.isComplete()).orElse(true);
  }
}
