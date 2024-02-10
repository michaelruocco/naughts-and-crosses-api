package uk.co.mruoc.nac.usecases;

import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.CreateGameRequest;
import uk.co.mruoc.nac.entities.Player;
import uk.co.mruoc.nac.entities.Players;
import uk.co.mruoc.nac.entities.RequestedPlayer;
import uk.co.mruoc.nac.entities.User;

@RequiredArgsConstructor
public class UserService {

  private final UserProvider provider;

  public Stream<User> getAll() {
    return provider.getAll();
  }

  public User get(String id) {
    return provider.get(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  public Players toPlayers(CreateGameRequest request) {
    return new Players(request.getRequestedPlayers().stream().map(this::toPlayer).toList());
  }

  private Player toPlayer(RequestedPlayer requestedPlayer) {
    return Player.builder()
        .user(get(requestedPlayer.getUserId()))
        .token(requestedPlayer.getToken())
        .build();
  }
}
