package uk.co.mruoc.nac.app.api.converter;

import uk.co.mruoc.nac.app.api.ApiCoordinates;
import uk.co.mruoc.nac.app.domain.entities.Coordinates;

public class ApiCoordinatesConverter {

    public Coordinates toCoordinates(ApiCoordinates apiCoordinates) {
        return Coordinates.builder()
                .x(apiCoordinates.getX())
                .y(apiCoordinates.getY())
                .build();
    }

    public ApiCoordinates toApiCoordinates(Coordinates coordinates) {
        return ApiCoordinates.builder()
                .x(coordinates.getX())
                .y(coordinates.getY())
                .build();
    }
}
