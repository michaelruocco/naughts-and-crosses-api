package uk.co.mruoc.nac.entities;

public class GameAlreadyCompleteException extends RuntimeException {

    public GameAlreadyCompleteException(long id) {
        super(String.format("game %s is already complete", id));
    }
}
