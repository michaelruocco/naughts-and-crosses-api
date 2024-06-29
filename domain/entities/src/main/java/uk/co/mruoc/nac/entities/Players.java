package uk.co.mruoc.nac.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Players {

  private final List<Player> values;

  @Getter private final int currentIndex;

  public Players(Player... values) {
    this(List.of(values));
  }

  public Players(Collection<Player> values) {
    this(new ArrayList<>(values), 0);
  }

  public Stream<Player> stream() {
    return values.stream();
  }

  public int size() {
    return values.size();
  }

  public Collection<Character> getDuplicateTokens() {
    Collection<Character> chars = values.stream().map(Player::getToken).toList();
    return chars.stream().filter(i -> Collections.frequency(chars, i) > 1).distinct().toList();
  }

  public void validatePlayerTurn(Turn turn) {
    Player currentPlayer = getCurrentPlayer().orElseThrow(() -> new NotPlayersTurnException(turn));
    if (!currentPlayer.hasUsername(turn.getUsername())) {
      throw new NotPlayersTurnException(turn.getUsername());
    }
    if (!currentPlayer.hasToken(turn.getToken())) {
      throw new NotPlayersTurnException(turn.getToken());
    }
  }

  public Optional<Character> getCurrentPlayerToken() {
    return getCurrentPlayer().map(Player::getToken);
  }

  public Players clearCurrentPlayer() {
    return toBuilder().currentIndex(-1).build();
  }

  public Players updateCurrentPlayer() {
    return toBuilder().currentIndex(nextTurnIndex()).build();
  }

  private Optional<Player> getCurrentPlayer() {
    if (currentIndex < 0) {
      return Optional.empty();
    }
    Player player = values.get(currentIndex);
    return Optional.of(player);
  }

  private int nextTurnIndex() {
    int nextIndex = currentIndex + 1;
    if (nextIndex >= values.size()) {
      return 0;
    }
    return nextIndex;
  }
}
