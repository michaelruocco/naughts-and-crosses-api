package uk.co.mruoc.nac.usecases;

public class NotEnoughPlayersException extends RuntimeException {

  public NotEnoughPlayersException(int numberOfPlayers, int minimumRequired) {
    super(toMessage(numberOfPlayers, minimumRequired));
  }

  private static String toMessage(int number, int minimum) {
    return String.format("only %d players supplied at least %d are required", number, minimum);
  }
}
