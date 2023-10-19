package uk.co.mruoc.nac.app.rest;

import java.util.UUID;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(UUID id) {
        super(String.format("game with id %s not found", id.toString()));
    }
}
