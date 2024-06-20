package uk.co.mruoc.nac.usecases;

public class CreateTokenFailedException extends RuntimeException {

  public CreateTokenFailedException(String username) {
    super(String.format("create token failed for username %s", username));
  }
}
