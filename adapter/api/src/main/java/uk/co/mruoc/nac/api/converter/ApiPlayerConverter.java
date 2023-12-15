package uk.co.mruoc.nac.api.converter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
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

  public Players toPlayers(Collection<ApiPlayer> apiPlayers) {
    return Players.builder()
        .values(apiPlayers.stream().map(this::toPlayer).toList())
        .currentIndex(-1)
        .build();
  }

  public Players toPlayers(Collection<ApiPlayer> apiPlayers, char nextPlayerToken) {
    List<Player> players = apiPlayers.stream().map(this::toPlayer).toList();
    return Players.builder()
        .values(players)
        .currentIndex(calculateCurrentIndex(players, nextPlayerToken))
        .build();
  }

  public Player toPlayer(ApiPlayer apiPlayer) {
    return Player.builder()
        .user(userConverter.toUser(apiPlayer.getUser()))
        .token(apiPlayer.getToken())
        .build();
  }

  private static int calculateCurrentIndex(List<Player> players, char nextPlayerToken) {
    Optional<Player> currentPlayer =
        players.stream().filter(player -> player.hasToken(nextPlayerToken)).findFirst();
    return currentPlayer.map(players::indexOf).orElse(-1);
  }
}
