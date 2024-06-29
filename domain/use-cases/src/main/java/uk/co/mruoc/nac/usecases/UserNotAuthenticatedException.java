package uk.co.mruoc.nac.usecases;

public class UserNotAuthenticatedException extends RuntimeException {

  public UserNotAuthenticatedException() {
    super("user not authenticated");
  }
}
