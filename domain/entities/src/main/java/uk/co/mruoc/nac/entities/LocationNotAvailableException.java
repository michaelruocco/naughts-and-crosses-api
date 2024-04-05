package uk.co.mruoc.nac.entities;

public class LocationNotAvailableException extends RuntimeException {

  public LocationNotAvailableException(Coordinates coordinates) {
    super(String.format("location %s is not available", coordinates.getKey()));
  }
}
