package uk.co.mruoc.nac.api.converter;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiPlayer;
import uk.co.mruoc.nac.api.dto.ApiStatus;
import uk.co.mruoc.nac.entities.Players;
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
        .draw(status.isDraw())
        .winner(status.getWinner().orElse(null))
        .nextPlayerToken(status.getCurrentPlayerToken().orElse(null))
        .build();
  }

  public Status toStatus(ApiStatus apiStatus, Collection<ApiPlayer> apiPlayers) {
    return Status.builder()
        .turn(apiStatus.getTurn())
        .complete(apiStatus.isComplete())
        .winner(apiStatus.getWinner().orElse(null))
        .players(toPlayers(apiStatus, apiPlayers))
        .build();
  }

  private Players toPlayers(ApiStatus apiStatus, Collection<ApiPlayer> apiPlayers) {
    return apiStatus
        .getNextPlayerToken()
        .map(token -> playerConverter.toPlayers(apiPlayers, token))
        .orElse(playerConverter.toPlayers(apiPlayers));
  }
}
