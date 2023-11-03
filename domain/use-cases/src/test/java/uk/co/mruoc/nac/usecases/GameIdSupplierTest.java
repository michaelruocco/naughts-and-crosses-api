package uk.co.mruoc.nac.usecases;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.LongSupplier;
import org.junit.jupiter.api.Test;

class GameIdSupplierTest {

    private final LongSupplier supplier = new GameIdSupplier();

    @Test
    void shouldReturnIncrementingLongValues() {
        assertThat(supplier.getAsLong()).isEqualTo(1);
        assertThat(supplier.getAsLong()).isEqualTo(2);
        assertThat(supplier.getAsLong()).isEqualTo(3);
    }
}
