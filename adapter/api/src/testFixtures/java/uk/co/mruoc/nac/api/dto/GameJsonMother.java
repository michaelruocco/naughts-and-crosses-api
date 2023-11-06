package uk.co.mruoc.nac.api.dto;

import static uk.co.mruoc.file.FileLoader.*;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameJsonMother {

    public static String initialGame1() {
        return loadContentFromClasspath("initial-game-1.json");
    }

    public static String xWinnerGame1() {
        return loadContentFromClasspath("x-winner-game-1.json");
    }
}
