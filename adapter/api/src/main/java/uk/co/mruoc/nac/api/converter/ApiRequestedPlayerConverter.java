package uk.co.mruoc.nac.api.converter;

import java.util.Collection;
import uk.co.mruoc.nac.api.dto.ApiRequestedPlayer;
import uk.co.mruoc.nac.entities.RequestedPlayer;

public class ApiRequestedPlayerConverter {
  public Collection<RequestedPlayer> toRequestedPlayers(
      Collection<ApiRequestedPlayer> apiRequestedPlayers) {
    return apiRequestedPlayers.stream().map(this::toRequestedPlayer).toList();
  }

  private RequestedPlayer toRequestedPlayer(ApiRequestedPlayer apiRequestedPlayer) {
    return RequestedPlayer.builder()
        .username(apiRequestedPlayer.getUsername())
        .token(apiRequestedPlayer.getToken())
        .build();
  }
}
