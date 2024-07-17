package uk.co.mruoc.nac.repository.postgres.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import uk.co.mruoc.nac.repository.postgres.user.OffsetBasedPageable.OffsetBasedPageableBuilder;

class OffsetBasedPageableTest {

  private final OffsetBasedPageableBuilder builder =
      OffsetBasedPageable.builder().limit(10).offset(0).sort(Sort.by(Sort.Order.asc("field1")));

  @ParameterizedTest
  @MethodSource("offsetAndExpectedPageNumber")
  void shouldCalculatePageNumberForOffset(long offset, int expectedPageNumber) {
    Pageable pageable = builder.offset(offset).build();

    int pageNumber = pageable.getPageNumber();

    assertThat(pageNumber).isEqualTo(expectedPageNumber);
  }

  @Test
  void shouldCalculateNextPageable() {
    Pageable pageable = builder.build();

    Pageable nextPageable = pageable.next();

    assertThat(nextPageable.getOffset()).isEqualTo(10);
    assertThat(nextPageable)
        .usingRecursiveComparison()
        .ignoringFields("offset")
        .isEqualTo(pageable);
  }

  @ParameterizedTest
  @MethodSource("offsetAndExpectedHasPrevious")
  void shouldReturnHasPrevious(int offset, boolean expectedHasPrevious) {
    Pageable pageable = builder.offset(offset).build();

    boolean hasPrevious = pageable.hasPrevious();

    assertThat(hasPrevious).isEqualTo(expectedHasPrevious);
  }

  @Test
  void shouldReturnFirstPage() {
    Pageable pageable = builder.offset(20).build();

    Pageable firstPageable = pageable.first();

    assertThat(firstPageable.getOffset()).isZero();
    assertThat(firstPageable)
        .usingRecursiveComparison()
        .ignoringFields("offset")
        .isEqualTo(pageable);
  }

  @ParameterizedTest
  @MethodSource("pageNumberAndExpectedOffset")
  void shouldReturnPageableByPageNumber(int pageNumber, int expectedOffset) {
    Pageable pageable = builder.build();

    Pageable otherPageable = pageable.withPage(pageNumber);

    assertThat(otherPageable.getOffset()).isEqualTo(expectedOffset);
    assertThat(otherPageable)
        .usingRecursiveComparison()
        .ignoringFields("offset")
        .isEqualTo(pageable);
  }

  @ParameterizedTest
  @MethodSource("offsetAndExpectedOffset")
  void shouldReturnPreviousOrFirst(int offset, int expectedOffset) {
    Pageable pageable = builder.offset(offset).build();

    Pageable otherPageable = pageable.previousOrFirst();

    assertThat(otherPageable.getOffset()).isEqualTo(expectedOffset);
    assertThat(otherPageable)
        .usingRecursiveComparison()
        .ignoringFields("offset")
        .isEqualTo(pageable);
  }

  private static Stream<Arguments> offsetAndExpectedPageNumber() {
    return Stream.of(
        Arguments.arguments(0, 0),
        Arguments.arguments(9, 0),
        Arguments.arguments(10, 1),
        Arguments.arguments(19, 1),
        Arguments.arguments(20, 2));
  }

  private static Stream<Arguments> offsetAndExpectedHasPrevious() {
    return Stream.of(
        Arguments.arguments(0, false),
        Arguments.arguments(9, false),
        Arguments.arguments(10, true));
  }

  private static Stream<Arguments> pageNumberAndExpectedOffset() {
    return Stream.of(
        Arguments.arguments(0, 0), Arguments.arguments(1, 10), Arguments.arguments(2, 20));
  }

  private static Stream<Arguments> offsetAndExpectedOffset() {
    return Stream.of(
        Arguments.arguments(0, 0),
        Arguments.arguments(9, 0),
        Arguments.arguments(10, 0),
        Arguments.arguments(19, 9),
        Arguments.arguments(20, 10),
        Arguments.arguments(29, 19),
        Arguments.arguments(30, 20));
  }
}
