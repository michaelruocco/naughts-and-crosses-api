package uk.co.mruoc.nac.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LocationTest {

  private final Location location = new Location(1, 2);

  @Test
  void shouldBeAvailableInitially() {
    assertThat(location.isAvailable()).isTrue();
  }

  @Test
  void shouldBeAvailableIfTokenIsSpace() {
    Location updatedLocation = location.withToken(' ');

    assertThat(updatedLocation.isAvailable()).isTrue();
  }

  @Test
  void shouldNotBeAvailableIfTokenIsNotSpace() {
    Location updatedLocation = location.withToken('X');

    assertThat(updatedLocation.isAvailable()).isFalse();
  }

  @Test
  void shouldReturnTrueIfXYCoordinatesMatch() {
    Coordinates coordinates = new Coordinates(1, 2);

    assertThat(location.isAt(coordinates)).isTrue();
  }

  @Test
  void shouldReturnFalseIfYCoordinateDoesNotMatch() {
    Coordinates coordinates = new Coordinates(1, 3);

    assertThat(location.isAt(coordinates)).isFalse();
  }

  @Test
  void shouldReturnFalseIfXCoordinateDoesNotMatch() {
    Coordinates coordinates = new Coordinates(3, 2);

    assertThat(location.isAt(coordinates)).isFalse();
  }

  @Test
  void shouldReturnTrueIfHasSameCoordinatesAsOtherLocation() {
    Location otherLocation = new Location(1, 2);

    assertThat(location.hasSameCoordinatesAs(otherLocation)).isTrue();
  }

  @Test
  void shouldReturnFalseIfDoesNotHaveSameCoordinatesAsOtherLocation() {
    Location otherLocation = new Location(3, 3);

    assertThat(location.hasSameCoordinatesAs(otherLocation)).isFalse();
  }
}
