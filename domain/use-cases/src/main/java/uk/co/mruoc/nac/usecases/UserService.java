package uk.co.mruoc.nac.usecases;

import java.util.Collection;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.CreateGameRequest;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.Player;
import uk.co.mruoc.nac.entities.Players;
import uk.co.mruoc.nac.entities.RequestedPlayer;
import uk.co.mruoc.nac.entities.User;

@RequiredArgsConstructor
public class UserService {

  private final ExternalUserService externalService;

  public Stream<User> getAll() {
    return externalService.getAll();
  }

  public User get(String id) {
    return externalService.get(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  public Players toPlayers(CreateGameRequest request) {
    return new Players(request.getRequestedPlayers().stream().map(this::toPlayer).toList());
  }

  public void create(Collection<CreateUserRequest> requests) {
    externalService.create(requests);
  }

  private Player toPlayer(RequestedPlayer requestedPlayer) {
    return Player.builder()
        .user(get(requestedPlayer.getUserId()))
        .token(requestedPlayer.getToken())
        .build();
  }
}
