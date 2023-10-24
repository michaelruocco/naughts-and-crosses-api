package uk.co.mruoc.nac.app.domain.usecases;

import uk.co.mruoc.nac.app.domain.entities.Game;

public interface GameEventPublisher {

    void created(Game game);
}
