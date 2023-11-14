package uk.co.mruoc.nac.api.converter;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiPlayer;
import uk.co.mruoc.nac.api.dto.ApiStatus;
import uk.co.mruoc.nac.entities.Status;

@RequiredArgsConstructor
public class ApiStatusConverter {

  private final ApiPlayerConverter playerConverter;

  public ApiStatusConverter() {
    this(new ApiPlayerConverter());
  }

  public ApiStatus toApiStatus(Status status) {
    return ApiStatus.builder()
        .turn(status.getTurn())
        .complete(status.isComplete())
        .nextPlayerToken(status.getCurrentPlayerToken().orElse(null))
        .build();
  }

  public Status toStatus(ApiStatus status, Collection<ApiPlayer> players) {
    return Status.builder()
        .complete(status.isComplete())
        .turn(status.getTurn())
        .players(playerConverter.toPlayers(players, status.getNextPlayerToken()))
        .build();
  }
}
