package uk.co.mruoc.nac.api.converter;

import uk.co.mruoc.nac.api.dto.ApiStatusPlayer;
import uk.co.mruoc.nac.entities.Player;

public class ApiStatusPlayerConverter {

  public ApiStatusPlayer toApiStatusPlayer(Player player) {
    return ApiStatusPlayer.builder()
        .username(player.getUsername())
        .fullName(player.getFullName())
        .token(player.getToken())
        .build();
  }
}
