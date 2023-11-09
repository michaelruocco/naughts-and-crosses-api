package uk.co.mruoc.nac.repository.postgres.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.repository.postgres.dto.DbGame;

@RequiredArgsConstructor
public class DbGameConverter {

  private final DbStatusConverter statusConverter;
  private final DbBoardConverter boardConverter;

  public DbGameConverter() {
    this(new DbStatusConverter(), new DbBoardConverter());
  }

  public DbGame toDbGame(Game game) {
    return DbGame.builder()
        .id(game.getId())
        .status(statusConverter.toDbStatus(game.getStatus()))
        .board(boardConverter.toDbBoard(game.getBoard()))
        .build();
  }

  public Game toGame(DbGame dbGame) {
    return Game.builder()
        .id(dbGame.getId())
        .status(statusConverter.toStatus(dbGame.getStatus()))
        .board(boardConverter.toBoard(dbGame.getBoard()))
        .build();
  }
}
