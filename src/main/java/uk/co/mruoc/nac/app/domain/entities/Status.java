package uk.co.mruoc.nac.app.domain.entities;

import java.util.Optional;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Status {

    private final boolean complete;
    private final Player winner;
    private final Player nextPlayer;

    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }
}
