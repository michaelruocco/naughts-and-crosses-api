package uk.co.mruoc.nac.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.Turn;
import uk.co.mruoc.nac.entities.User;

@Slf4j
@RequiredArgsConstructor
public class TurnTaker {

  private final AuthenticatedUserSupplier userSupplier;

  public Game takeTurn(Game game, Turn turn) {
    User user = userSupplier.get();
    Turn userTurn = turn.withUsername(user.getUsername());
    log.info("taking turn for game with id {} {}", game.getId(), userTurn);
    return game.take(userTurn);
  }
}
