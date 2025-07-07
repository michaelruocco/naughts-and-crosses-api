package uk.co.mruoc.nac.usecases;

public class CannotAccessOtherUserException extends RuntimeException {

  public CannotAccessOtherUserException(String username, String otherUsername) {
    super(String.format("user %s cannot access other user %s", username, otherUsername));
  }
}
