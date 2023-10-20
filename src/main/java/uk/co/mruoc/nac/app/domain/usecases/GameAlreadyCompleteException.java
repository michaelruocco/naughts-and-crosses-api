package uk.co.mruoc.nac.app.domain.usecases;

import java.util.UUID;

public class GameAlreadyCompleteException extends RuntimeException {

    public GameAlreadyCompleteException(UUID id) {
        super(String.format("game with id %s already complete", id.toString()));
    }
}
