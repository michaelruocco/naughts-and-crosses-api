package uk.co.mruoc.nac.entities;

public class NotPlayersTurnException extends RuntimeException {

  public NotPlayersTurnException(String username) {
    super(String.format("not turn for username %s", username));
  }
}
