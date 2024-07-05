package uk.co.mruoc.nac.entities;

public class UserNotGamePlayerException extends RuntimeException {

  public UserNotGamePlayerException(User user, Game game) {
    super(String.format("user %s is not a player for game %s", user, game));
  }
}
