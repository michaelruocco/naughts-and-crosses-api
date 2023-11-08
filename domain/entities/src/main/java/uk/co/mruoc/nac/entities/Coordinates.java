package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Coordinates {

  private final long x;
  private final long y;

  public String getKey() {
    return String.format("%d-%d", x, y);
  }

  public boolean sameAs(Coordinates otherCoordinates) {
    return x == otherCoordinates.getX() && y == otherCoordinates.getY();
  }
}
