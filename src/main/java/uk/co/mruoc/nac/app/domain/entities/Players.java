package uk.co.mruoc.nac.app.domain.entities;

import java.util.List;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.domain.usecases.NotPlayersTurnException;

@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Players {

    private final List<Player> values;
    private final int currentIndex;

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
        return getCurrentPlayerToken() == token;
    }

    public char getCurrentPlayerToken() {
        Player player = values.get(currentIndex);
        return player.getToken();
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
}
