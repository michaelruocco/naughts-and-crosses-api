package uk.co.mruoc.nac.entities;

public class LocationNotAvailableException extends RuntimeException {

  public LocationNotAvailableException(Coordinates coordinates) {
    super(String.format("board location at %s is not available", coordinates.getKey()));
  }
}
