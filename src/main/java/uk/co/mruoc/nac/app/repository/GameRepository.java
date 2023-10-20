package uk.co.mruoc.nac.app.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.domain.entities.Game;

@RequiredArgsConstructor
public class GameRepository {

    private final Map<UUID, Game> games;

    public GameRepository() {
        this(new HashMap<>());
    }

    public Optional<Game> find(UUID id) {
        return Optional.ofNullable(games.get(id));
    }

    public void save(Game game) {
        games.put(game.getId(), game);
    }
}
