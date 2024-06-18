package uk.co.mruoc.nac.usecases;

import uk.co.mruoc.nac.entities.Game;

public interface GameEventPublisher {

  void updated(Game game);

  void deleted(long id);
}
