package uk.co.mruoc.nac.app.domain.usecases;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.app.domain.entities.Board;
import uk.co.mruoc.nac.app.domain.entities.Turn;

class ConsoleBoardFormatterTest {

    private final ConsoleBoardFormatter formatter = new ConsoleBoardFormatter();

    @Test
    void shouldReturnBoardStateAsFormattedString() {
        Board board = new Board()
                .update(new Turn(0, 0, 'X'))
                .update(new Turn(1, 0, 'O'))
                .update(new Turn(2, 0, 'X'))
                .update(new Turn(0, 1, 'O'))
                .update(new Turn(1, 1, 'X'))
                .update(new Turn(2, 1, 'O'))
                .update(new Turn(0, 2, 'X'))
                .update(new Turn(1, 2, 'O'))
                .update(new Turn(2, 2, 'X'));

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
