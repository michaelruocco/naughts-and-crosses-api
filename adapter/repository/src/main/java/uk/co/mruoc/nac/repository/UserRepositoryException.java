package uk.co.mruoc.nac.repository;

public class UserRepositoryException extends RuntimeException {

  public UserRepositoryException(String message) {
    super(message);
  }

  public UserRepositoryException(Throwable cause) {
    super(cause);
  }
}
