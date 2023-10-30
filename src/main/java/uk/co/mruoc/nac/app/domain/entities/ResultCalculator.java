package uk.co.mruoc.nac.app.domain.entities;

import java.util.Collection;
import java.util.Collections;

public class ResultCalculator {

    public Result calculate(Board board, char token) {
        Collection<Coordinates> coordinates = board.findWinner(token);
        if (coordinates.isEmpty()) {
            return winnerNotFound(board);
        }
        return winnerFound(coordinates, token);
    }

    private Result winnerNotFound(Board board) {
        return Result.builder()
                .winningLine(Collections.emptyList())
                .draw(board.isFull())
                .build();
    }

    private Result winnerFound(Collection<Coordinates> coordinates, char token) {
        return Result.builder()
                .winningLine(coordinates)
                .winningToken(token)
                .draw(false)
                .build();
    }
}
