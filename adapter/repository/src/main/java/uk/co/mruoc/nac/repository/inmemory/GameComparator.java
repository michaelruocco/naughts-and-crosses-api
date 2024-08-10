package uk.co.mruoc.nac.repository.inmemory;

import java.util.Comparator;
import uk.co.mruoc.nac.entities.Game;

public class GameComparator implements Comparator<Game> {

  @Override
  public int compare(Game g1, Game g2) {
    return Long.compare(g2.getId(), g1.getId());
  }
}
