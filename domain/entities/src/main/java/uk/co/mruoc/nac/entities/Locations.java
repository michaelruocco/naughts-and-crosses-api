package uk.co.mruoc.nac.entities;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Locations {

  public static Map<String, Location> toMap(Collection<Location> locations) {
    return locations.stream()
        .collect(
            Collectors.toMap(
                Location::getKey, Function.identity(), (x, y) -> y, LinkedHashMap::new));
  }
}
