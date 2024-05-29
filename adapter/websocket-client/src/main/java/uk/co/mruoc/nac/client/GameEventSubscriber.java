package uk.co.mruoc.nac.client;

import java.util.Optional;
import java.util.stream.Stream;

public interface GameEventSubscriber<T> {

  String getDestination();

  default T forceGetMostRecent() {
    return getMostRecent().orElseThrow();
  }

  default Optional<T> getMostRecent() {
    return getAll().reduce((first, second) -> second);
  }

  Stream<T> getAll();
}
