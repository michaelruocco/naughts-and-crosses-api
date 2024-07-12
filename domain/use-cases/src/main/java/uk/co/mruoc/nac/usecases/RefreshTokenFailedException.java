package uk.co.mruoc.nac.usecases;

public class RefreshTokenFailedException extends RuntimeException {

  public RefreshTokenFailedException(String token) {
    super(String.format("refresh token failed for token %s", token));
  }
}
