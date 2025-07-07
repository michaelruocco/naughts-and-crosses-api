package uk.co.mruoc.nac.usecases;

public class VerifySoftwareTokenFailedException extends RuntimeException {

  public VerifySoftwareTokenFailedException(String status) {
    super(String.format("verify software token returned status %s", status));
  }
}
