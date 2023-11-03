package uk.co.mruoc.nac.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.usecases.GameRepository;

@RequiredArgsConstructor
public class InMemoryGameRepository implements GameRepository {

    private final Map<Long, Game> games;

    public InMemoryGameRepository() {
        this(new HashMap<>());
    }

    @Override
    public Optional<Game> find(long id) {
        return Optional.ofNullable(games.get(id));
    }

    @Override
    public void save(Game game) {
        games.put(game.getId(), game);
    }

    @Override
    public Stream<Game> getAll() {
        return games.values().stream().sorted(new GameComparator());
    }
}
