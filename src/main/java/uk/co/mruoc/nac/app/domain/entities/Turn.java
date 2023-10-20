package uk.co.mruoc.nac.app.domain.entities;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Data
public class Turn {

    private final Coordinates coordinates;
    private final char token;

    public Turn(int x, int y, char token) {
        this(new Coordinates(x, y), token);
    }
}
