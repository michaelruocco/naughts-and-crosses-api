package uk.co.mruoc.nac.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SortOrderTest {

  @Test
  void shouldReturnTrueIfAscending() {
    SortOrder order = SortOrder.asc("field1");

    boolean ascending = order.isAscending();

    assertThat(ascending).isTrue();
  }

  @Test
  void shouldReturnFalseIfNotAscending() {
    SortOrder order = SortOrder.desc("field2");

    boolean ascending = order.isAscending();

    assertThat(ascending).isFalse();
  }
}
