package uk.co.mruoc.nac.app.domain.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
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
        Map<String, Location> updatedLocations = new LinkedHashMap<>(locations);
        updatedLocations.put(updatedLocation.getKey(), updatedLocation);
        return Collections.unmodifiableMap(updatedLocations);
    }

    public boolean isFull() {
        return locations.values().stream().allMatch(Predicate.not(Location::isAvailable));
    }

    public Collection<Coordinates> findWinner(char token) {
        Collection<Coordinates> winner = findVerticalWinner(token);
        if (!winner.isEmpty()) {
            return winner;
        }

        winner = findHorizontalWinner(token);
        if (!winner.isEmpty()) {
            return winner;
        }

        winner = findForwardSlashWinner(token);
        if (!winner.isEmpty()) {
            return winner;
        }

        winner = findBackSlashWinner(token);
        if (!winner.isEmpty()) {
            return winner;
        }
        return Collections.emptyList();
    }

    private Collection<Coordinates> findVerticalWinner(char token) {
        for (int x = 0; x < size; x++) {
            Collection<Coordinates> coordinates = findWinningColumn(x, token);
            if (coordinates.size() == size) {
                return coordinates;
            }
        }
        return Collections.emptyList();
    }

    private Collection<Coordinates> findWinningColumn(int x, char token) {
        Collection<Coordinates> coordinates = new ArrayList<>();
        for (int y = 0; y < size; y++) {
            if (doesLocationContain(x, y, token)) {
                coordinates.add(new Coordinates(x, y));
            } else {
                return Collections.emptyList();
            }
        }
        return coordinates;
    }

    private Collection<Coordinates> findHorizontalWinner(char token) {
        for (int y = 0; y < size; y++) {
            Collection<Coordinates> coordinates = findWinningRow(y, token);
            if (coordinates.size() == size) {
                return coordinates;
            }
        }
        return Collections.emptyList();
    }

    private Collection<Coordinates> findWinningRow(int y, char token) {
        Collection<Coordinates> coordinates = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            if (doesLocationContain(x, y, token)) {
                coordinates.add(new Coordinates(x, y));
            } else {
                return Collections.emptyList();
            }
        }
        return coordinates;
    }

    private Collection<Coordinates> findForwardSlashWinner(char token) {
        Collection<Coordinates> coordinates = new ArrayList<>();
        int y = 0;
        int x = size - 1;
        do {
            if (doesLocationContain(x, y, token)) {
                coordinates.add(new Coordinates(x, y));
            } else {
                return Collections.emptyList();
            }
            y++;
            x--;
        } while (y <= size && x >= 0);
        return coordinates;
    }

    private Collection<Coordinates> findBackSlashWinner(char token) {
        Collection<Coordinates> coordinates = new ArrayList<>();
        int y = 0;
        int x = 0;
        do {
            if (doesLocationContain(x, y, token)) {
                coordinates.add(new Coordinates(x, y));
            } else {
                return Collections.emptyList();
            }
            y++;
            x++;
        } while (y < size && x < size);
        return coordinates;
    }

    private static Map<String, Location> buildLocations(long size) {
        Map<String, Location> locations = new LinkedHashMap<>();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Location location = new Location(x, y);
                locations.put(location.getKey(), location);
            }
        }
        return Collections.unmodifiableMap(locations);
    }
}
