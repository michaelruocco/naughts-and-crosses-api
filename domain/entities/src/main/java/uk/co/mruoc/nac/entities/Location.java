package uk.co.mruoc.nac.entities;

import lombok.AccessLevel;
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

  @With private final char token;

  @With(value = AccessLevel.PRIVATE)
  private final boolean winner;

  public Location(long x, long y) {
    this(new Coordinates(x, y), AVAILABLE);
  }

  public Location(Coordinates coordinates, char token) {
    this(coordinates, token, false);
  }

  public boolean hasSameCoordinatesAs(Location otherLocation) {
    return isAt(otherLocation.getCoordinates());
  }

  public Location toWinner() {
    return withWinner(true);
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
