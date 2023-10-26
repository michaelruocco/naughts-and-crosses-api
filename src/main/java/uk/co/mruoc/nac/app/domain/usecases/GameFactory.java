package uk.co.mruoc.nac.app.domain.usecases;

import java.util.function.LongSupplier;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.domain.entities.Board;
import uk.co.mruoc.nac.app.domain.entities.Game;
import uk.co.mruoc.nac.app.domain.entities.Status;

@RequiredArgsConstructor
public class GameFactory {

    private final LongSupplier idSupplier;

    public GameFactory() {
        this(new GameIdSupplier());
    }

    public Game buildGame() {
        return Game.builder()
                .id(idSupplier.getAsLong())
                .status(new Status())
                .board(new Board(3))
                .build();
    }
}
