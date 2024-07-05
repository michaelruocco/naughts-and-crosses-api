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

  public void validate(Turn turn) {
    String username = turn.getUsername();
    Player currentPlayer =
        getCurrentPlayer().orElseThrow(() -> new NotPlayersTurnException(username));
    if (!currentPlayer.hasUsername(username)) {
      throw new NotPlayersTurnException(username);
    }
    if (!currentPlayer.hasToken(turn.getToken())) {
      throw new IncorrectTokenForPlayerException(turn);
    }
  }

  public Players clearCurrentPlayer() {
    return toBuilder().currentIndex(-1).build();
  }

  public Players updateCurrentPlayer() {
    return toBuilder().currentIndex(nextTurnIndex()).build();
  }

  public Optional<Player> getCurrentPlayer() {
    if (currentIndex < 0) {
      return Optional.empty();
    }
    Player player = values.get(currentIndex);
    return Optional.of(player);
  }

  public Player getPlayerByToken(Character token) {
    return stream()
        .filter(p -> p.hasToken(token))
        .findFirst()
        .orElseThrow(() -> new PlayerWithTokenNotFound(token));
  }

  public boolean containsUsername(String username) {
    return stream().anyMatch(user -> user.hasUsername(username));
  }

  private int nextTurnIndex() {
    int nextIndex = currentIndex + 1;
    if (nextIndex >= values.size()) {
      return 0;
    }
    return nextIndex;
  }
}
