package uk.co.mruoc.nac.app.domain.usecases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.domain.entities.Board;
import uk.co.mruoc.nac.app.domain.entities.Location;

@RequiredArgsConstructor
public class BoardFactory {

    public Board buildBoard(int size) {
        return Board.builder().size(3).locations(locations(size)).build();
    }

    private static Collection<Location> locations(int size) {
        List<Location> locations = new ArrayList<>();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                locations.add(new Location(x, y));
            }
        }
        return Collections.unmodifiableList(locations);
    }
}
