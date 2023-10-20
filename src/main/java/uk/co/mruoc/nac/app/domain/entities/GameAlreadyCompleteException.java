package uk.co.mruoc.nac.app.domain.entities;

import java.util.UUID;

public class GameAlreadyCompleteException extends RuntimeException {

    public GameAlreadyCompleteException(UUID id) {
        super(String.format("game %s is already complete", id.toString()));
    }
}
