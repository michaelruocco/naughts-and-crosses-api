package uk.co.mruoc.nac.entities;

public class IncorrectTokenForPlayerException extends RuntimeException {

  public IncorrectTokenForPlayerException(Turn turn) {
    this(turn.getToken(), turn.getUsername());
  }

  public IncorrectTokenForPlayerException(char token, String username) {
    super(String.format("incorrect token %s for user %s", token, username));
  }
}
