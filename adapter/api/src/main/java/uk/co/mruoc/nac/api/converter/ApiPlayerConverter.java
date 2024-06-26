package uk.co.mruoc.nac.api.converter;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiPlayer;
import uk.co.mruoc.nac.entities.Player;
import uk.co.mruoc.nac.entities.Players;

@RequiredArgsConstructor
public class ApiPlayerConverter {

  private final ApiUserConverter userConverter;

  public ApiPlayerConverter() {
    this(new ApiUserConverter());
  }

  public Collection<ApiPlayer> toApiPlayers(Players players) {
    return players.stream().map(this::toApiPlayer).toList();
  }

  public ApiPlayer toApiPlayer(Player player) {
    return ApiPlayer.builder()
        .user(userConverter.toApiUser(player.getUser()))
        .token(player.getToken())
        .build();
  }
}
