package uk.co.mruoc.nac.app.domain.usecases;

import uk.co.mruoc.nac.app.domain.entities.Coordinates;

public class LocationNotAvailableException extends RuntimeException {

    public LocationNotAvailableException(Coordinates coordinates) {
        super(String.format("board location at %s is not available", coordinates.getKey()));
    }
}
