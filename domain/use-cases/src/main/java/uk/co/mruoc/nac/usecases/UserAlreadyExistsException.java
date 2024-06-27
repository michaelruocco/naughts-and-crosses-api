package uk.co.mruoc.nac.usecases;

public class UserAlreadyExistsException extends RuntimeException {

  public UserAlreadyExistsException(String username) {
    super(String.format("user %s already exists", username));
  }
}
