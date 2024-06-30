package uk.co.mruoc.nac.entities;

public class IncorrectTokenForPlayerException extends RuntimeException {
  public IncorrectTokenForPlayerException(Turn turn) {
    super(String.format("incorrect token %s for user %s", turn.getToken(), turn.getUsername()));
  }
}
