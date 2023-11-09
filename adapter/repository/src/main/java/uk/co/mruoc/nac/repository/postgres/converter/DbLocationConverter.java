package uk.co.mruoc.nac.repository.postgres.converter;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.Location;
import uk.co.mruoc.nac.repository.postgres.dto.DbLocation;

@RequiredArgsConstructor
public class DbLocationConverter {

  private final DbCoordinatesConverter coordinatesConverter;

  public DbLocationConverter() {
    this(new DbCoordinatesConverter());
  }

  public Collection<DbLocation> toDbLocations(Collection<Location> locations) {
    return locations.stream().map(this::toDbLocation).toList();
  }

  public DbLocation toDbLocation(Location location) {
    return DbLocation.builder()
        .token(location.getToken())
        .winner(location.isWinner())
        .coordinates(coordinatesConverter.toDbCoordinates(location.getCoordinates()))
        .build();
  }

  public Collection<Location> toLocations(Collection<DbLocation> locations) {
    return locations.stream().map(this::toLocation).toList();
  }

  public Location toLocation(DbLocation dbLocation) {
    return Location.builder()
        .token(dbLocation.getToken())
        .winner(dbLocation.isWinner())
        .coordinates(coordinatesConverter.toCoordinates(dbLocation.getCoordinates()))
        .build();
  }
}
