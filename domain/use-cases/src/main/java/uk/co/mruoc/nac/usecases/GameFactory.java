package uk.co.mruoc.nac.usecases;

import java.util.function.LongSupplier;
import lombok.Builder;
import uk.co.mruoc.nac.entities.Board;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.Players;
import uk.co.mruoc.nac.entities.Status;

@Builder
public class GameFactory {

  private final LongSupplier idSupplier;
  private final PlayerFactory playerFactory;

  public Game buildGame() {
    return buildGame(playerFactory.build());
  }

  public Game buildGame(Players players) {
    return Game.builder()
        .id(idSupplier.getAsLong())
        .status(new Status(players))
        .board(new Board())
        .build();
  }
}
