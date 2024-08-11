package uk.co.mruoc.nac.repository.inmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.GamePageRequest;

@RequiredArgsConstructor
public class GamePageRequestPredicate implements Predicate<Game> {

  private final Predicate<Game> predicate;

  public GamePageRequestPredicate(GamePageRequest request) {
    this(toPredicate(request));
  }

  @Override
  public boolean test(Game game) {
    return predicate.test(game);
  }

  private static Predicate<Game> toPredicate(GamePageRequest request) {
    Collection<Predicate<Game>> predicates = new ArrayList<>();
    request.getUsername().map(GamePlayerPredicate::new).ifPresent(predicates::add);
    request.getComplete().map(GameCompletePredicate::new).ifPresent(predicates::add);
    return predicates.stream().reduce(x -> true, Predicate::and);
  }
}
