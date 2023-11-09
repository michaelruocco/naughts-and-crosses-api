package uk.co.mruoc.nac.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

@Builder
@RequiredArgsConstructor
@Slf4j
@ToString
public class Board {

  private static final int DEFAULT_SIZE = 3;

  @Getter private final int size;

  @With(value = AccessLevel.PRIVATE)
  private final Map<String, Location> locations;

  public Board() {
    this(DEFAULT_SIZE);
  }

  public Board(int size) {
    this(size, buildLocations(size));
  }

  public Board(int size, Collection<Location> locations) {
    this(size, toMap(locations));
  }

  public Board update(Turn turn) {
    Location location = getLocationIfAvailable(turn.getCoordinates());
    Board updated = update(location.withToken(turn.getToken()));
    Collection<Coordinates> winningLine = updated.findWinningLine(turn.getToken());
    if (winningLine.isEmpty()) {
      return updated;
    }
    return updated.recordWinningLine(winningLine);
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

  public boolean isPlayable(char token) {
    return !hasWinner(token) && !isFull();
  }

  public boolean isEmpty() {
    return locations.values().stream().allMatch(Location::isAvailable);
  }

  public boolean isFull() {
    return locations.values().stream().noneMatch(Location::isAvailable);
  }

  private Board recordWinningLine(Collection<Coordinates> winningLine) {
    Collection<Location> winningLocations =
        winningLine.stream()
            .map(this::getLocation)
            .flatMap(Optional::stream)
            .map(Location::toWinner)
            .toList();
    return update(winningLocations);
  }

  private Location getLocationIfAvailable(Coordinates coordinates) {
    return getLocation(coordinates)
        .filter(Location::isAvailable)
        .orElseThrow(() -> new LocationNotAvailableException(coordinates));
  }

  private Board update(Location changedLocation) {
    return update(List.of(changedLocation));
  }

  private Board update(Collection<Location> changedLocations) {
    return withLocations(toUpdatedLocations(changedLocations));
  }

  private Map<String, Location> toUpdatedLocations(Collection<Location> changedLocations) {
    Map<String, Location> updatedLocations = new LinkedHashMap<>(locations);
    changedLocations.forEach(l -> updatedLocations.put(l.getKey(), l));
    return Collections.unmodifiableMap(updatedLocations);
  }

  public boolean hasWinner(char token) {
    return !findWinningLine(token).isEmpty();
  }

  public Collection<Coordinates> findWinningLine(char token) {
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

  private static Collection<Location> buildLocations(long size) {
    Collection<Location> locations = new ArrayList<>();
    for (int y = 0; y < size; y++) {
      for (int x = 0; x < size; x++) {
        locations.add(new Location(x, y));
      }
    }
    return Collections.unmodifiableCollection(locations);
  }

  private static Map<String, Location> toMap(Collection<Location> locations) {
    return locations.stream()
        .collect(
            Collectors.toMap(
                Location::getKey, Function.identity(), (x, y) -> y, LinkedHashMap::new));
  }
}
