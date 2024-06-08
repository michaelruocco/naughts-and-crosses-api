package uk.co.mruoc.nac.usecases;

public class UserNotFoundByUsernameException extends RuntimeException {

  public UserNotFoundByUsernameException(String username) {
    super(String.format("user with username %s not found", username));
  }
}
