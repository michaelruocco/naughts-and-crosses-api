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
    private final int size;

    @With(value = AccessLevel.PRIVATE)
    private final Map<String, Location> locations;

    public Board() {
        this(DEFAULT_SIZE);
    }

    public Board(int size) {
        this(size, buildLocations(size));
    }

    public Board update(Turn turn) {
        Location location = getLocationIfAvailable(turn.getCoordinates());
        return update(location.withToken(turn.getToken()));
    }

    public Collection<Location> getLocations() {
        return locations.values();
    }

    public boolean doesLocationContain(int x, int y, char otherToken) {
        Optional<Character> token = getLocation(new Coordinates(x, y)).map(Location::getToken);
        return token.map(t -> t == otherToken).orElse(false);
    }

    public char getToken(int x, int y) {
        return getLocation(new Coordinates(x, y)).map(Location::getToken).orElse(' ');
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

    public boolean hasWinner(char token) {
        if (hasVerticalWinner(token)) {
            return true;
        }
        if (hasHorizontalWinner(token)) {
            return true;
        }
        if (hasForwardSlashWinner(token)) {
            return true;
        }
        return hasBackSlashWinner(token);
    }

    private boolean hasVerticalWinner(char token) {
        for (int x = 0; x < size; x++) {
            if (winsColumn(x, token)) {
                return true;
            }
        }
        return false;
    }

    private boolean winsColumn(int x, char token) {
        for (int y = 0; y < size; y++) {
            if (!doesLocationContain(x, y, token)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasHorizontalWinner(char token) {
        for (int y = 0; y < size; y++) {
            if (winsRow(y, token)) {
                return true;
            }
        }
        return false;
    }

    private boolean winsRow(int y, char token) {
        for (int x = 0; x < size; x++) {
            if (!doesLocationContain(x, y, token)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasForwardSlashWinner(char token) {
        int y = 0;
        int x = size - 1;
        do {
            if (!doesLocationContain(x, y, token)) {
                return false;
            }
            y++;
            x--;
        } while (y <= size && x >= 0);
        return true;
    }

    private boolean hasBackSlashWinner(char token) {
        int y = 0;
        int x = 0;
        do {
            if (!doesLocationContain(x, y, token)) {
                return false;
            }
            y++;
            x++;
        } while (y < size && x < size);
        return true;
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
