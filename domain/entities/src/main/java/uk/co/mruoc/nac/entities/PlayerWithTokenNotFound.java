package uk.co.mruoc.nac.entities;

public class PlayerWithTokenNotFound extends RuntimeException {
  public PlayerWithTokenNotFound(char token) {
    super(Character.toString(token));
  }
}
