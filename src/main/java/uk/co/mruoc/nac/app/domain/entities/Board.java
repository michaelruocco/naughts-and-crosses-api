package uk.co.mruoc.nac.app.domain.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.With;
import uk.co.mruoc.nac.app.domain.usecases.LocationNotAvailableException;

@RequiredArgsConstructor
@Data
public class Board {

    private final long size;

    @With(value = AccessLevel.PRIVATE)
    private final Collection<Location> locations;

    public Board(long size) {
        this(size, buildLocations(size));
    }

    public Board update(Turn turn) {
        Location location = getLocationIfAvailable(turn.getCoordinates());
        return update(location.withToken(turn.getToken()));
    }

    private Location getLocationIfAvailable(Coordinates coordinates) {
        return findLocation(coordinates)
                .filter(Location::isAvailable)
                .orElseThrow(() -> new LocationNotAvailableException(coordinates));
    }

    private Optional<Location> findLocation(Coordinates coordinates) {
        return locations.stream().filter(location -> location.isAt(coordinates)).findFirst();
    }

    private Board update(Location updatedLocation) {
        return withLocations(toUpdatedLocations(updatedLocation));
    }

    private Collection<Location> toUpdatedLocations(Location updatedLocation) {
        return locations.stream()
                .map(location -> replaceIfRequired(location, updatedLocation))
                .toList();
    }

    private static Location replaceIfRequired(Location location, Location updatedLocation) {
        if (location.hasSameCoordinatesAs(updatedLocation)) {
            return updatedLocation;
        }
        return location;
    }

    private static Collection<Location> buildLocations(long size) {
        List<Location> locations = new ArrayList<>();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                locations.add(new Location(x, y));
            }
        }
        return Collections.unmodifiableList(locations);
    }
}
