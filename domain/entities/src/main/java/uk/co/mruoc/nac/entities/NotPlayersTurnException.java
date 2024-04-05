package uk.co.mruoc.nac.entities;

public class NotPlayersTurnException extends RuntimeException {

  public NotPlayersTurnException(char token) {
    super(String.format("not %s players turn", token));
  }
}
