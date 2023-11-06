package uk.co.mruoc.nac.api.dto;

import static uk.co.mruoc.file.FileLoader.*;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameJsonMother {

    public static String initial() {
        return loadContentFromClasspath("game/initial.json");
    }

    public static String xWinner() {
        return loadContentFromClasspath("game/x-winner.json");
    }

    public static String draw() {
        return loadContentFromClasspath("game/draw.json");
    }
}
