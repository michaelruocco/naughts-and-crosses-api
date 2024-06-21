package uk.co.mruoc.nac.entities;

public class UserWithIdAlreadyExistsException extends RuntimeException {

  public UserWithIdAlreadyExistsException(String id) {
    super(String.format("user with id %s already exists", id));
  }
}
