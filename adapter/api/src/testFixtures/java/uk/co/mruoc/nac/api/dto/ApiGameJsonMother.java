package uk.co.mruoc.nac.api.dto;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static uk.co.mruoc.file.FileLoader.loadContentFromClasspath;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiGameJsonMother {

  public static String initial() {
    return loadContentFromClasspath("game/initial.json");
  }

  public static String xTurn() {
    return loadContentFromClasspath("game/x-turn.json");
  }

  public static String xWinner() {
    return loadContentFromClasspath("game/x-winner.json");
  }

  public static String draw() {
    return loadContentFromClasspath("game/draw.json");
  }
}
