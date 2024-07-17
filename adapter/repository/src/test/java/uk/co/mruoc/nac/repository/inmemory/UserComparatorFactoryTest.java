package uk.co.mruoc.nac.repository.inmemory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Set;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.entities.SortNotSupportedException;
import uk.co.mruoc.nac.entities.SortOrder;

class UserComparatorFactoryTest {

  private final UserComparatorFactory factory = new UserComparatorFactory();

  @Test
  void shouldThrowExceptionIfSortFieldNotSupported() {
    String fieldName = "unsupported-field";

    Throwable error = catchThrowable(() -> factory.toComparator(Set.of(SortOrder.asc(fieldName))));

    assertThat(error).isInstanceOf(SortNotSupportedException.class).hasMessage(fieldName);
  }
}
