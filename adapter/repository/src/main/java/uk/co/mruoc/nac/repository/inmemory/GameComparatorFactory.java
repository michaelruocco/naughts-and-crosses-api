package uk.co.mruoc.nac.repository.inmemory;

import java.util.Comparator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.Game;

@Slf4j
public class GameComparatorFactory extends AbstractComparatorFactory<Game> {

  public GameComparatorFactory() {
    super(buildFieldComparators(), Comparator.comparing(Game::getId));
  }

  private static Map<String, Comparator<Game>> buildFieldComparators() {
    return Map.of("id", Comparator.comparing(Game::getId));
  }
}
