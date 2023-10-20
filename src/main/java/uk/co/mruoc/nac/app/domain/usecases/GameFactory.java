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
        this(() -> UUID.fromString("5b6bff66-7361-4d8b-9eb1-d7b3500a6bf3"));
    }

    public Game buildGame() {
        return Game.builder()
                .id(idSupplier.get())
                .status(new Status())
                .board(new Board(3))
                .build();
    }
}
