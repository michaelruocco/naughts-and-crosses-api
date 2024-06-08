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

  public Players(Stream<Player> stream) {
    this(stream.toList());
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

  public void validateIsTurn(char token) {
    if (!isTurn(token)) {
      throw new NotPlayersTurnException(token);
    }
  }

  public boolean isTurn(char token) {
    return getCurrentPlayerToken().map(currentToken -> currentToken == token).orElse(false);
  }

  public Optional<Character> getCurrentPlayerToken() {
    if (currentIndex < 0) {
      return Optional.empty();
    }
    Player player = values.get(currentIndex);
    return Optional.of(player.getToken());
  }

  public Players clearCurrentPlayer() {
    return toBuilder().currentIndex(-1).build();
  }

  public Players updateCurrentPlayer() {
    return toBuilder().currentIndex(nextTurnIndex()).build();
  }

  public Players add(Player player) {
    return new Players(Stream.concat(values.stream(), Stream.of(player)));
  }

  private int nextTurnIndex() {
    int nextIndex = currentIndex + 1;
    if (nextIndex >= values.size()) {
      return 0;
    }
    return nextIndex;
  }
}
