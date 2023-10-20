package uk.co.mruoc.nac.app.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public boolean isAt(Location otherLocation) {
        return isAt(otherLocation.getCoordinates());
    }

    public boolean isAt(Coordinates otherCoordinates) {
        return coordinates.getX() == otherCoordinates.getX() && coordinates.getY() == otherCoordinates.getY();
    }

    @JsonIgnore
    public boolean isAvailable() {
        return token == AVAILABLE;
    }
}
