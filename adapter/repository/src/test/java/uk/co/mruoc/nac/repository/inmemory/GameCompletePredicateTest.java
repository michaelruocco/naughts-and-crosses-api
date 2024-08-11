package uk.co.mruoc.nac.repository.inmemory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.co.mruoc.nac.entities.Game;

class GameCompletePredicateTest {

  @ParameterizedTest
  @MethodSource("completeFilterAndExpectedResult")
  void shouldReturnExpectedResultForSearchTerm(Boolean complete, boolean expectedResult) {
    Predicate<Game> predicate = new GameCompletePredicate(complete);
    Game game = mock(Game.class);
    when(game.isComplete()).thenReturn(true);

    boolean result = predicate.test(game);

    assertThat(result).isEqualTo(expectedResult);
  }

  private static Stream<Arguments> completeFilterAndExpectedResult() {
    return Stream.of(
        Arguments.of(true, true), Arguments.of(false, false), Arguments.of(null, true));
  }
}
