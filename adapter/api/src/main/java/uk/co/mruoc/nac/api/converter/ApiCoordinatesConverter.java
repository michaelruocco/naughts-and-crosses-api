package uk.co.mruoc.nac.api.converter;

import uk.co.mruoc.nac.api.dto.ApiCoordinates;
import uk.co.mruoc.nac.entities.Coordinates;

public class ApiCoordinatesConverter {

  public Coordinates toCoordinates(ApiCoordinates apiCoordinates) {
    return Coordinates.builder().x(apiCoordinates.getX()).y(apiCoordinates.getY()).build();
  }

  public ApiCoordinates toApiCoordinates(Coordinates coordinates) {
    return ApiCoordinates.builder().x(coordinates.getX()).y(coordinates.getY()).build();
  }
}
