package uk.co.mruoc.nac.app.domain.entities;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.With;

@RequiredArgsConstructor
@Builder
@Data
public class Location {

    private static final char AVAILABLE = ' ';

    private final Coordinates coordinates;

    @With
    private final char token;

    public Location(long x, long y) {
        this(new Coordinates(x, y), AVAILABLE);
    }

    public boolean hasSameCoordinatesAs(Location otherLocation) {
        return isAt(otherLocation.getCoordinates());
    }

    public boolean isAt(Coordinates otherCoordinates) {
        return coordinates.sameAs(otherCoordinates);
    }

    public boolean isAvailable() {
        return token == AVAILABLE;
    }

    public String getKey() {
        return coordinates.getKey();
    }
}
