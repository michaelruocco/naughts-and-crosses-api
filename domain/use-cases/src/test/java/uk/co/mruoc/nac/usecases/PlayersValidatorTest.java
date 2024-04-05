package uk.co.mruoc.nac.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.entities.PlayerMother;
import uk.co.mruoc.nac.entities.Players;

class PlayersValidatorTest {

  private final PlayersValidator validator = new PlayersValidator();

  @Test
  void shouldThrowExceptionIfLessThanTwoPlayersProvided() {
    Players players = PlayerMother.empty();

    Throwable error = catchThrowable(() -> validator.validate(players));

    assertThat(error).isInstanceOf(NotEnoughPlayersException.class);
  }

  @Test
  void shouldThrowExceptionIfTokenIsDuplicated() {
    Players players = PlayerMother.withTokens('X', 'X');

    Throwable error = catchThrowable(() -> validator.validate(players));

    assertThat(error).isInstanceOf(MultiplePlayersCannotUseTheSameTokenException.class);
  }
}
