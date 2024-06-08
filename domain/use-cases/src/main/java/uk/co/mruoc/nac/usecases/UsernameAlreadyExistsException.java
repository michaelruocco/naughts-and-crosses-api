package uk.co.mruoc.nac.usecases;

public class UsernameAlreadyExistsException extends RuntimeException {

  public UsernameAlreadyExistsException(String username) {
    super(String.format("user with username %s already exists", username));
  }
}
