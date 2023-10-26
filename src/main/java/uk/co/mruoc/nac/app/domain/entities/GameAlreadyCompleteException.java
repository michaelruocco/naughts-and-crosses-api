package uk.co.mruoc.nac.app.domain.entities;

public class GameAlreadyCompleteException extends RuntimeException {

    public GameAlreadyCompleteException(long id) {
        super(String.format("game %s is already complete", id));
    }
}
