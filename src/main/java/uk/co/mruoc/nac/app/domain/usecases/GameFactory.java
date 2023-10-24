package uk.co.mruoc.nac.app.domain.usecases;

import java.util.UUID;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.domain.entities.Board;
import uk.co.mruoc.nac.app.domain.entities.Game;
import uk.co.mruoc.nac.app.domain.entities.Status;

@RequiredArgsConstructor
public class GameFactory {

    private final Supplier<UUID> idSupplier;

    public GameFactory() {
        this(UUID::randomUUID);
    }

    public Game buildGame() {
        return Game.builder()
                .id(idSupplier.get())
                .status(new Status())
                .board(new Board(3))
                .build();
    }
}
