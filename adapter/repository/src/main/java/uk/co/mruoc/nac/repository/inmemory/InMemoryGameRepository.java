package uk.co.mruoc.nac.repository.inmemory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.GamePage;
import uk.co.mruoc.nac.entities.GamePageRequest;
import uk.co.mruoc.nac.usecases.GameRepository;

@RequiredArgsConstructor
@Slf4j
public class InMemoryGameRepository implements GameRepository {

  private final Map<Long, Game> games;
  private final ComparatorFactory<Game> comparatorFactory;

  public InMemoryGameRepository() {
    this(new HashMap<>(), new GameComparatorFactory());
  }

  @Override
  public Optional<Game> get(long id) {
    return Optional.ofNullable(games.get(id));
  }

  @Override
  public void create(Game game) {
    save(game);
  }

  @Override
  public void update(Game game) {
    save(game);
  }

  @Override
  public Stream<Game> getAll() {
    return games.values().stream().sorted(new GameComparator());
  }

  @Override
  public void deleteAll() {
    games.clear();
  }

  @Override
  public void delete(long id) {
    games.remove(id);
  }

  @Override
  public GamePage getPage(GamePageRequest request) {
    log.info("returning page of users for request {}", request);
    Collection<Game> filteredGames = toFilteredGames(request);
    return GamePage.builder()
        .total(filteredGames.size())
        .games(toPage(filteredGames, request))
        .build();
  }

  private void save(Game game) {
    games.put(game.getId(), game);
  }

  private Collection<Game> toFilteredGames(GamePageRequest request) {
    return games.values().stream()
        .filter(new GamePageRequestPredicate(request))
        .sorted(comparatorFactory.toComparator(request.getSort()))
        .toList();
  }

  private static Collection<Game> toPage(Collection<Game> filteredGames, GamePageRequest request) {
    return filteredGames.stream().skip(request.getOffset()).limit(request.getLimit()).toList();
  }
}
