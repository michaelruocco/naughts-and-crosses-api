package uk.co.mruoc.nac.app.domain.entities;

import java.util.Collection;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import uk.co.mruoc.nac.app.domain.usecases.LocationNotAvailableException;

@Builder(toBuilder = true)
@Data
public class Board {

    private final long size;
    private final Collection<Location> locations;

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
        Collection<Location> updatedLocations = locations.stream()
                .map(location -> replaceIfMatches(location, updatedLocation))
                .toList();
        return toBuilder().locations(updatedLocations).build();
    }

    private static Location replaceIfMatches(Location location, Location updatedLocation) {
        if (location.isAt(updatedLocation)) {
            return updatedLocation;
        }
        return location;
    }
}
