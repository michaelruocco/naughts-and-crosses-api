package uk.co.mruoc.nac.app.rest;

import uk.co.mruoc.nac.app.api.ApiCoordinates;

import java.util.UUID;

public class LocationNotAvailableException extends RuntimeException {

    public LocationNotAvailableException(ApiCoordinates coordinates) {
        super(String.format("board location at %s is not available", coordinates.getKey()));
    }
}
