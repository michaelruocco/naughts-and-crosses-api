package uk.co.mruoc.nac.app.domain.entities;

import java.util.Collection;
import java.util.Objects;
import lombok.Builder;

@Builder
public class Result {

    private final Character winningToken;
    private final Collection<Coordinates> winningLine;
    private final boolean draw;

    public boolean hasWinner() {
        return Objects.nonNull(winningToken);
    }
}
