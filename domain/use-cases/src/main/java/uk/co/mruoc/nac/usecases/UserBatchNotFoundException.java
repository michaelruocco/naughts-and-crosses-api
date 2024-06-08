package uk.co.mruoc.nac.usecases;

public class UserBatchNotFoundException extends RuntimeException {

  public UserBatchNotFoundException(String id) {
    super(String.format("user batch with id %s not found", id));
  }
}
