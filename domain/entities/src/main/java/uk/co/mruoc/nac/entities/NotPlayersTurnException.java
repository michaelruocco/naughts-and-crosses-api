package uk.co.mruoc.nac.entities;

public class NotPlayersTurnException extends RuntimeException {

  public NotPlayersTurnException(Turn turn) {
    super(
        String.format(
            "not turn for token %s and username %s", turn.getToken(), turn.getUsername()));
  }

  public NotPlayersTurnException(String username) {
    super(String.format("not turn for username %s", username));
  }

  public NotPlayersTurnException(char token) {
    super(String.format("not turn for token %s", token));
  }
}
