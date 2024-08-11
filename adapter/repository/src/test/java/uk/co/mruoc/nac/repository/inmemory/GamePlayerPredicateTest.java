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

class GamePlayerPredicateTest {

  @ParameterizedTest
  @MethodSource("usernameAndExpectedResult")
  void shouldReturnExpectedResultForSearchTerm(String username, boolean expectedResult) {
    Predicate<Game> predicate = new GamePlayerPredicate(username);
    Game game = mock(Game.class);
    when(game.getPlayerUsernames()).thenReturn(Stream.of("user-1"));

    boolean result = predicate.test(game);

    assertThat(result).isEqualTo(expectedResult);
  }

  private static Stream<Arguments> usernameAndExpectedResult() {
    return Stream.of(
        Arguments.of("user-1", true), Arguments.of("admin", false), Arguments.of(null, true));
  }
}
