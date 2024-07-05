package uk.co.mruoc.nac.entities;

public class UserNotGamePlayerException extends RuntimeException {

  public UserNotGamePlayerException(String username, long gameId) {
    super(String.format("user %s is not a player of game %s", username, gameId));
  }
}
