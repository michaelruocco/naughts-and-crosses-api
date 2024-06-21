package uk.co.mruoc.nac.entities;

public class UserNotFoundByIdException extends RuntimeException {

  public UserNotFoundByIdException(String id) {
    super(String.format("user with id %s not found", id));
  }
}
