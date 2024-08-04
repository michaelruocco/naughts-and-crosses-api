package uk.co.mruoc.nac.repository.inmemory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.entities.UserMother;
import uk.co.mruoc.nac.entities.UserNameMother;

class SearchTermPredicateTest {

  @ParameterizedTest
  @MethodSource("searchTermAndExpectedResult")
  void shouldReturnExpectedResultForSearchTerm(String searchTerm, boolean expectedResult) {
    Predicate<User> predicate = new SearchTermPredicate(searchTerm);
    User user = UserMother.admin();

    boolean result = predicate.test(user);

    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  void shouldHandleNullSearchFields() {
    Predicate<User> predicate = new SearchTermPredicate("empty-test");
    User user =
        UserMother.adminBuilder().name(UserNameMother.empty()).email(null).username(null).build();

    boolean result = predicate.test(user);

    assertThat(result).isFalse();
  }

  private static Stream<Arguments> searchTermAndExpectedResult() {
    return Stream.of(
        Arguments.of("ad", true),
        Arguments.of("ema", true),
        Arguments.of("", true),
        Arguments.of(null, true),
        Arguments.of("Admin", true),
        Arguments.of("user", true),
        Arguments.of("blah", false));
  }
}
