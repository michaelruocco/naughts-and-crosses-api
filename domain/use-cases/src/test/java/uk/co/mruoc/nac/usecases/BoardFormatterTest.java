package uk.co.mruoc.nac.usecases;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.entities.Board;
import uk.co.mruoc.nac.entities.Player;
import uk.co.mruoc.nac.entities.PlayerMother;
import uk.co.mruoc.nac.entities.Turn;

class BoardFormatterTest {

  private static final Player CROSSES_PLAYER = PlayerMother.crossesPlayer();
  private static final Player NAUGHTS_PLAYER = PlayerMother.naughtsPlayer();

  private final BoardFormatter formatter = new BoardFormatter();

  @Test
  void shouldReturnBoardStateAsFormattedString() {
    Board board =
        new Board()
            .update(new Turn(0, 0, CROSSES_PLAYER))
            .update(new Turn(1, 0, NAUGHTS_PLAYER))
            .update(new Turn(2, 0, CROSSES_PLAYER))
            .update(new Turn(0, 1, NAUGHTS_PLAYER))
            .update(new Turn(1, 1, CROSSES_PLAYER))
            .update(new Turn(2, 1, NAUGHTS_PLAYER))
            .update(new Turn(0, 2, CROSSES_PLAYER))
            .update(new Turn(1, 2, NAUGHTS_PLAYER))
            .update(new Turn(2, 2, CROSSES_PLAYER));

    String formatted = formatter.format(board);

    assertThat(formatted)
        .isEqualTo(
            """
                            0 1 2
                          0 X O X
                          1 O X O
                          2 X O X""");
  }
}
