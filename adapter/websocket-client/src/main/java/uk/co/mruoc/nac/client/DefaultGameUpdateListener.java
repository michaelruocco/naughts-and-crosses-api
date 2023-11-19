package uk.co.mruoc.nac.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiGame;

@RequiredArgsConstructor
public class DefaultGameUpdateListener implements GameUpdateListener {

  private final Collection<ApiGame> updates;

  public DefaultGameUpdateListener() {
    this(new ArrayList<>());
  }

  @Override
  public void updated(ApiGame game) {
    updates.add(game);
  }

  public void reset() {
    updates.clear();
  }

  public ApiGame forceGetMostRecentUpdate() {
    return getMostRecentUpdate().orElseThrow();
  }

  public Optional<ApiGame> getMostRecentUpdate() {
    return getAllUpdates().reduce((first, second) -> second);
  }

  public Stream<ApiGame> getAllUpdates() {
    return updates.stream();
  }
}
