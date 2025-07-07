package uk.co.mruoc.nac.usecases;

public class CreateAccessTokenFailedException extends RuntimeException {

  public CreateAccessTokenFailedException(String username) {
    super(String.format("create access token failed for %s", username));
  }
}
