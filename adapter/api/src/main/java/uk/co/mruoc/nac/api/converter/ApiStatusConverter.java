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
        .nextPlayerToken(status.getCurrentPlayerToken().orElse(null))
        .build();
  }

  public Status toStatus(ApiStatus apiStatus, Collection<ApiPlayer> apiPlayers) {
    return Status.builder()
        .complete(apiStatus.isComplete())
        .turn(apiStatus.getTurn())
        .players(toPlayers(apiStatus, apiPlayers))
        .build();
  }

  private Players toPlayers(ApiStatus apiStatus, Collection<ApiPlayer> apiPlayers) {
    return apiStatus
        .getNextPlayerToken()
        .map(nextPlayerToken -> playerConverter.toPlayers(apiPlayers, nextPlayerToken))
        .orElse(playerConverter.toPlayers(apiPlayers));
  }
}
