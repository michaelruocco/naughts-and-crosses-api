package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Data
public class Turn {

  private final Coordinates coordinates;
  private final char token;
  private final String username;

  public Turn(int x, int y, Player player) {
    this(new Coordinates(x, y), player.getToken(), player.getUsername());
  }

  public Turn(Coordinates coordinates, Player player) {
    this(coordinates, player.getToken(), player.getUsername());
  }

  public char getToken() {
    return token;
  }
}
