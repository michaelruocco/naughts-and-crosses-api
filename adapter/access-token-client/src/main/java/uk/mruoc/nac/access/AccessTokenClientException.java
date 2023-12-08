package uk.mruoc.nac.access;

public class AccessTokenClientException extends RuntimeException {

  public AccessTokenClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public AccessTokenClientException(String message) {
    super(message);
  }
}
