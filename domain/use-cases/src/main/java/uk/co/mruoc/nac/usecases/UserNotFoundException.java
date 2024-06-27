package uk.co.mruoc.nac.usecases;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String username) {
    super(String.format("user %s not found", username));
  }
}
