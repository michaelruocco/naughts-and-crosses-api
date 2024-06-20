package uk.co.mruoc.nac.user.inmemory;

public class KeyLoaderException extends RuntimeException {

  public KeyLoaderException(String message, Throwable cause) {
    super(message, cause);
  }

  public KeyLoaderException(Throwable cause) {
    super(cause);
  }
}
