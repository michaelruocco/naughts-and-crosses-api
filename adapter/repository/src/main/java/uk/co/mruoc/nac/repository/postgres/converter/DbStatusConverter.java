package uk.co.mruoc.nac.repository.postgres.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.Status;
import uk.co.mruoc.nac.repository.postgres.dto.DbStatus;

@RequiredArgsConstructor
public class DbStatusConverter {

  private final DbPlayerConverter playerConverter;

  public DbStatusConverter() {
    this(new DbPlayerConverter());
  }

  public DbStatus toDbStatus(Status status) {
    return DbStatus.builder()
        .turn(status.getTurn())
        .complete(status.isComplete())
        .players(playerConverter.toDbPlayers(status.getPlayers()))
        .winner(status.getWinner().orElse(null))
        .build();
  }

  public Status toStatus(DbStatus dbStatus) {
    return Status.builder()
        .turn(dbStatus.getTurn())
        .complete(dbStatus.isComplete())
        .players(playerConverter.toPlayers(dbStatus.getPlayers()))
        .winner(dbStatus.getWinner())
        .build();
  }
}
