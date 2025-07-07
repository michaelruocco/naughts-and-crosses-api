package uk.co.mruoc.nac.usecases;

public class RespondToChallengeFailedException extends RuntimeException {

  public RespondToChallengeFailedException(Throwable cause) {
    super(cause);
  }

  public RespondToChallengeFailedException(String message) {
    super(message);
  }
}
