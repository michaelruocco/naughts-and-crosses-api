package uk.co.mruoc.nac.usecases;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.Players;

@RequiredArgsConstructor
public class PlayersValidator {

  private final int minimumRequired;

  public PlayersValidator() {
    this(2);
  }

  public void validate(Players players) {
    validateEnoughPlayers(players);
    validateNoDuplicateTokens(players);
  }

  private void validateEnoughPlayers(Players players) {
    int numberOfPlayers = players.size();
    if (numberOfPlayers < minimumRequired) {
      throw new NotEnoughPlayersException(numberOfPlayers, minimumRequired);
    }
  }

  private void validateNoDuplicateTokens(Players players) {
    Collection<Character> duplicateTokens = players.getDuplicateTokens();
    if (!duplicateTokens.isEmpty()) {
      throw new MultiplePlayersCannotUseTheSameTokenException(duplicateTokens);
    }
  }
}
