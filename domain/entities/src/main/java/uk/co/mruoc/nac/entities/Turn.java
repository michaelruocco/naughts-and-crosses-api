package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.With;

@Builder
@RequiredArgsConstructor
@Data
public class Turn {

  private final Coordinates coordinates;
  private final char token;
  @With private final String username;

  public Turn(int x, int y, char token) {
    this(new Coordinates(x, y), token, null);
  }
}
