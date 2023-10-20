package uk.co.mruoc.nac.app.domain.entities;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.domain.usecases.PlayerNotFoundException;

@RequiredArgsConstructor
public class Players {

    private final Collection<Player> values;

    public Players(Player... values) {
        this(List.of(values));
    }

    public Player getPlayer(char token) {
        return stream()
                .filter(p -> p.getToken() == token)
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException(token));
    }

    public Player getNextPlayer(char token) {
        return stream().filter(p -> p.getToken() != token).findFirst().orElseThrow();
    }

    public Stream<Player> stream() {
        return values.stream();
    }
}
