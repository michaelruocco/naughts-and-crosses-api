package uk.co.mruoc.nac.usecases;

import static uk.co.mruoc.nac.entities.PlayersCollector.playersCollector;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.CreateGameRequest;
import uk.co.mruoc.nac.entities.Player;
import uk.co.mruoc.nac.entities.Players;
import uk.co.mruoc.nac.entities.RequestedPlayer;

@RequiredArgsConstructor
public class PlayerFactory {

  private final UserService userService;

  public Players toPlayers(CreateGameRequest request) {
    return request.getRequestedPlayers().stream().map(this::toPlayer).collect(playersCollector());
  }

  private Player toPlayer(RequestedPlayer requestedPlayer) {
    return Player.builder()
        .user(userService.getById(requestedPlayer.getUserId()))
        .token(requestedPlayer.getToken())
        .build();
  }
}
