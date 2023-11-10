package uk.co.mruoc.nac.repository.inmemory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.usecases.IdSupplier;

class InMemoryIdSupplierTest {

  private final IdSupplier supplier = new InMemoryIdSupplier();

  @Test
  void shouldReturnIncrementingLongValues() {
    assertThat(supplier.getAsLong()).isEqualTo(1);
    assertThat(supplier.getAsLong()).isEqualTo(2);
    assertThat(supplier.getAsLong()).isEqualTo(3);
  }

  @Test
  void shouldResetValues() {
    assertThat(supplier.getAsLong()).isEqualTo(1);
    assertThat(supplier.getAsLong()).isEqualTo(2);
    supplier.reset();
    assertThat(supplier.getAsLong()).isEqualTo(1);
  }
}
