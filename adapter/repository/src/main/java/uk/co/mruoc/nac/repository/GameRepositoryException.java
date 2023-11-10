package uk.co.mruoc.nac.repository;

public class GameRepositoryException extends RuntimeException {

  public GameRepositoryException(String message) {
    super(message);
  }

  public GameRepositoryException(Throwable cause) {
    super(cause);
  }
}
