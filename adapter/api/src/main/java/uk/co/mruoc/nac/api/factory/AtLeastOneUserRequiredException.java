package uk.co.mruoc.nac.api.factory;

public class AtLeastOneUserRequiredException extends RuntimeException {

  public AtLeastOneUserRequiredException() {
    super("at least one user must be provided to create a game");
  }
}
