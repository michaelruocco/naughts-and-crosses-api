package uk.co.mruoc.nac.app.repository;

import java.util.Comparator;
import uk.co.mruoc.nac.app.domain.entities.Game;

public class GameComparator implements Comparator<Game> {

    @Override
    public int compare(Game g1, Game g2) {
        return g1.getId().compareTo(g2.getId());
    }
}
