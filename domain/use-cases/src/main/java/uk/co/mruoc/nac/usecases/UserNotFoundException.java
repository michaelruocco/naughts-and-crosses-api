package uk.co.mruoc.nac.usecases;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String id) {
    super(String.format("user with id %s not found", id));
  }
}
