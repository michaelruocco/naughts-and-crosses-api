package uk.co.mruoc.nac.usecases;

public class SetMfaPreferenceFailedException extends RuntimeException {

  public SetMfaPreferenceFailedException(String accessToken) {
    super(String.format("failed to set mfa preference using access token %s", accessToken));
  }
}
