package uk.co.mruoc.nac.entities;

public class VirusScanException extends RuntimeException {

  public VirusScanException(String message) {
    super(message);
  }

  public VirusScanException(Throwable cause) {
    super(cause);
  }
}
