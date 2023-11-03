package uk.co.mruoc.nac.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CoordinatesTest {

    private final Coordinates coordinates = new Coordinates(1, 2);

    @Test
    void shouldReturnTrueIfSameXY() {
        assertThat(coordinates.sameAs(new Coordinates(1, 2))).isTrue();
    }

    @Test
    void shouldReturnFalseIfDifferentX() {
        assertThat(coordinates.sameAs(new Coordinates(3, 2))).isFalse();
    }

    @Test
    void shouldReturnFalseIfDifferentY() {
        assertThat(coordinates.sameAs(new Coordinates(1, 3))).isFalse();
    }

    @Test
    void shouldReturnKey() {
        assertThat(coordinates.getKey()).isEqualTo("1-2");
    }
}
