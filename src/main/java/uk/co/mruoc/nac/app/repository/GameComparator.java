package uk.co.mruoc.nac.app.repository;

import java.util.Comparator;
import uk.co.mruoc.nac.app.domain.entities.Game;

public class GameComparator implements Comparator<Game> {

    @Override
    public int compare(Game g1, Game g2) {
        return Long.compare(g1.getId(), g2.getId());
    }
}
