package uk.co.mruoc.nac.app.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.app.config.cors.AllowedOriginsSupplier;

class AllowedOriginsSupplierTest {

  @Test
  void shouldReturnMultipleCommaSeparatedOriginsAsAnArray() {
    Supplier<String[]> supplier =
        new AllowedOriginsSupplier("http://localhost:8081,http://localhost:8082");

    String[] origins = supplier.get();

    assertThat(origins).containsExactly("http://localhost:8081", "http://localhost:8082");
  }

  @Test
  void shouldReturnSingleOriginAsAnArray() {
    Supplier<String[]> supplier = new AllowedOriginsSupplier("http://localhost:8081");

    String[] origins = supplier.get();

    assertThat(origins).containsExactly("http://localhost:8081");
  }

  @Test
  void shouldReturnEmptyArrayForEmptyStringInput() {
    Supplier<String[]> supplier = new AllowedOriginsSupplier("");

    String[] origins = supplier.get();

    assertThat(origins).isEmpty();
  }

  @Test
  void shouldReturnEmptyArrayForNullInput() {
    Supplier<String[]> supplier = new AllowedOriginsSupplier((String) null);

    String[] origins = supplier.get();

    assertThat(origins).isEmpty();
  }
}
