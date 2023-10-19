package uk.co.mruoc.nac.app.rest;

public class NotPlayersTurnException extends RuntimeException {

    public NotPlayersTurnException(char token) {
        super(String.format("player with token %s is not next player so cannot take turn", token));
    }
}
