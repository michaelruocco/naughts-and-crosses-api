package uk.co.mruoc.nac.app.domain.entities;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;

@RequiredArgsConstructor
public class Board {

    private static final int DEFAULT_SIZE = 3;

    @Getter
    private final long size;

    @With(value = AccessLevel.PRIVATE)
    private final Map<String, Location> locations;

    public Board() {
        this(DEFAULT_SIZE);
    }

    public Board(long size) {
        this(size, buildLocations(size));
    }

    public Board update(Turn turn) {
        Location location = getLocationIfAvailable(turn.getCoordinates());
        return update(location.withToken(turn.getToken()));
    }

    public Collection<Location> getLocations() {
        return locations.values();
    }

    public Optional<Location> getLocation(Coordinates coordinates) {
        return Optional.ofNullable(locations.get(coordinates.getKey()));
    }

    private Location getLocationIfAvailable(Coordinates coordinates) {
        return getLocation(coordinates)
                .filter(Location::isAvailable)
                .orElseThrow(() -> new LocationNotAvailableException(coordinates));
    }

    private Board update(Location updatedLocation) {
        return withLocations(toUpdatedLocations(updatedLocation));
    }

    private Map<String, Location> toUpdatedLocations(Location updatedLocation) {
        Map<String, Location> updatedLocations = new HashMap<>(locations);
        updatedLocations.put(updatedLocation.getKey(), updatedLocation);
        return Collections.unmodifiableMap(updatedLocations);
    }

    private static Map<String, Location> buildLocations(long size) {
        Map<String, Location> locations = new HashMap<>();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Location location = new Location(x, y);
                locations.put(location.getKey(), location);
            }
        }
        return Collections.unmodifiableMap(locations);
    }
}
