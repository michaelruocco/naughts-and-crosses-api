package uk.co.mruoc.nac.user.inmemory;

public class InvalidJwtException extends RuntimeException {

  public InvalidJwtException(String message) {
    super(message);
  }
}
