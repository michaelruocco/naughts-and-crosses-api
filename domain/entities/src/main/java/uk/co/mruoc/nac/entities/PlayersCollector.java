package uk.co.mruoc.nac.entities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayersCollector {

  public static GenericCollector<Player, Players> playersCollector() {
    return new GenericCollector<>(Players::new);
  }
}
