package uk.co.mruoc.nac.entities;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Players {

    private final List<Player> values;
    private final int currentIndex;

    public Players() {
        this(player1(), player2());
    }

    public Players(Player... values) {
        this(List.of(values), 0);
    }

    public Stream<Player> stream() {
        return values.stream();
    }

    public void validateIsTurn(char token) {
        if (!isTurn(token)) {
            throw new NotPlayersTurnException(token);
        }
    }

    public boolean isTurn(char token) {
        return getCurrentPlayerToken()
                .map(currentToken -> currentToken == token)
                .orElse(false);
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

    private int nextTurnIndex() {
        int nextIndex = currentIndex + 1;
        if (nextIndex >= values.size()) {
            return 0;
        }
        return nextIndex;
    }

    private static Player player1() {
        return Player.builder().name("Player 1").token('X').build();
    }

    private static Player player2() {
        return Player.builder().name("Player 2").token('O').build();
    }
}
