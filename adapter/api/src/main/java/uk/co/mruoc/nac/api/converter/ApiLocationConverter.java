package uk.co.mruoc.nac.api.converter;

import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiLocation;
import uk.co.mruoc.nac.entities.Location;
import uk.co.mruoc.nac.entities.Locations;

@RequiredArgsConstructor
public class ApiLocationConverter {

  private final ApiCoordinatesConverter coordinatesConverter;

  public ApiLocationConverter() {
    this(new ApiCoordinatesConverter());
  }

  public Collection<ApiLocation> toApiLocations(Collection<Location> locations) {
    return locations.stream().map(this::toApiLocation).toList();
  }

  public ApiLocation toApiLocation(Location location) {
    return ApiLocation.builder()
        .coordinates(coordinatesConverter.toApiCoordinates(location.getCoordinates()))
        .token(location.getToken())
        .winner(location.isWinner())
        .build();
  }

  public Map<String, Location> toLocationsMap(Collection<ApiLocation> apiLocations) {
    return Locations.toMap(toLocations(apiLocations));
  }

  public Collection<Location> toLocations(Collection<ApiLocation> apiLocations) {
    return apiLocations.stream().map(this::toLocation).toList();
  }

  public Location toLocation(ApiLocation apiLocation) {
    return Location.builder()
        .coordinates(coordinatesConverter.toCoordinates(apiLocation.getCoordinates()))
        .token(apiLocation.getToken())
        .winner(apiLocation.isWinner())
        .build();
  }
}
