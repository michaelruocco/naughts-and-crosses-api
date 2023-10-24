package uk.co.mruoc.nac.app.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.domain.entities.Game;
import uk.co.mruoc.nac.app.domain.usecases.GameRepository;

@RequiredArgsConstructor
public class InMemoryGameRepository implements GameRepository {

    private final Map<UUID, Game> games;

    public InMemoryGameRepository() {
        this(new HashMap<>());
    }

    @Override
    public Optional<Game> find(UUID id) {
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
