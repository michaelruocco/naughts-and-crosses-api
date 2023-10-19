package uk.co.mruoc.nac.app.rest;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(char token) {
        super(String.format("player with token %s not found", token));
    }
}
