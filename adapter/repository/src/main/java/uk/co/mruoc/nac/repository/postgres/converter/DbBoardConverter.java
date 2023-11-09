package uk.co.mruoc.nac.repository.postgres.converter;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.Board;
import uk.co.mruoc.nac.entities.Location;
import uk.co.mruoc.nac.repository.postgres.dto.DbBoard;

@RequiredArgsConstructor
public class DbBoardConverter {

  private final DbLocationConverter locationConverter;

  public DbBoardConverter() {
    this(new DbLocationConverter());
  }

  public DbBoard toDbBoard(Board board) {
    return DbBoard.builder()
        .size(board.getSize())
        .locations(locationConverter.toDbLocations(board.getLocations()))
        .build();
  }

  public Board toBoard(DbBoard dbBoard) {
    Collection<Location> locations = locationConverter.toLocations(dbBoard.getLocations());
    return new Board(dbBoard.getSize(), locations);
  }
}
