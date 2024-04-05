package uk.co.mruoc.nac.entities;

public class LocationNotFoundException extends RuntimeException {

  public LocationNotFoundException(Coordinates coordinates) {
    super(String.format("location %s not found", coordinates.getKey()));
  }
}
