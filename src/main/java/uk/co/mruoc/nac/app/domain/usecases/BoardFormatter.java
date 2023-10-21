package uk.co.mruoc.nac.app.domain.usecases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import uk.co.mruoc.nac.app.domain.entities.Board;

public class BoardFormatter {

    public String format(Board board) {
        int size = board.getSize();
        Collection<String> rows = new ArrayList<>();
        rows.add(buildHeader(size));
        for (int y = 0; y < size; y++) {
            rows.add(buildRow(board, size, y));
        }
        return rows.stream().collect(Collectors.joining(System.lineSeparator()));
    }

    private static String buildHeader(int size) {
        String header = IntStream.range(0, size).mapToObj(Integer::toString).collect(Collectors.joining(" "));
        return String.format("  %s", header);
    }

    private static String buildRow(Board board, int size, int y) {
        Collection<String> tokens = new ArrayList<>();
        tokens.add(Integer.toString(y));
        for (int x = 0; x < size; x++) {
            tokens.add(Character.toString(board.getToken(x, y)));
        }
        return String.join(" ", tokens);
    }
}
