package uk.co.mruoc.nac.app.domain.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Turn {

    private final Coordinates coordinates;
    private final char token;
}
