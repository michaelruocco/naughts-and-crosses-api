package uk.co.mruoc.nac.usecases;

public class RefreshAccessTokenFailedException extends RuntimeException {

  public RefreshAccessTokenFailedException(String token) {
    super(String.format("refresh access token failed for token %s", token));
  }
}
