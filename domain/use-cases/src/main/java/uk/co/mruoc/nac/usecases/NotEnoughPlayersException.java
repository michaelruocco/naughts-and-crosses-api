package uk.co.mruoc.nac.usecases;

public class NotEnoughPlayersException extends RuntimeException {

  public NotEnoughPlayersException(int numberOfPlayers, int minimumRequired) {
    super(
        String.format(
            "only %d players supplied at least %d are required", numberOfPlayers, minimumRequired));
  }
}
