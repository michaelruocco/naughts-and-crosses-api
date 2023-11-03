package uk.co.mruoc.nac.usecases;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(long id) {
        super(String.format("game with id %s not found", id));
    }
}
