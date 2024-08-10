package uk.co.mruoc.nac.app;

import java.util.Collection;
import java.util.Set;
import lombok.Builder;
import uk.co.mruoc.nac.api.dto.ApiCreateGameRequest;
import uk.co.mruoc.nac.api.dto.ApiCreateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.api.dto.ApiTurn;
import uk.co.mruoc.nac.api.dto.ApiUser;
import uk.co.mruoc.nac.api.factory.ApiCreateGameRequestFactory;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;

@Builder
public class Fixtures {

  private final NaughtsAndCrossesApiClient adminClient;
  private final NaughtsAndCrossesApiClient user1Client;
  private final NaughtsAndCrossesApiClient user2Client;
  private final ApiCreateGameRequestFactory requestFactory;

  public ApiGame givenGameExists() {
    ApiCreateGameRequest request = buildCreateGameRequest();
    return adminClient.createGame(request);
  }

  public ApiGame givenCompletedGameExists() {
    ApiGame game = givenGameExists();
    long id = game.getId();

    user1Client.takeTurn(id, new ApiTurn(0, 0, 'X'));
    user2Client.takeTurn(id, new ApiTurn(0, 1, 'O'));
    user1Client.takeTurn(id, new ApiTurn(1, 0, 'X'));
    user2Client.takeTurn(id, new ApiTurn(1, 1, 'O'));
    return user1Client.takeTurn(id, new ApiTurn(2, 0, 'X'));
  }

  public ApiUser givenUserExists() {
    ApiCreateUserRequest request =
        ApiCreateUserRequest.builder()
            .username("test-user")
            .firstName("Test")
            .lastName("User")
            .email("test.user@email.com")
            .emailVerified(true)
            .groups(Set.of())
            .build();
    return givenUserExists(request);
  }

  public ApiUser givenUserExists(ApiCreateUserRequest request) {
    return adminClient.createUser(request);
  }

  public ApiCreateGameRequest buildCreateGameRequest() {
    Collection<ApiUser> users =
        adminClient.getAllUsers().stream()
            .filter(user -> user.getGroups().contains("player"))
            .toList();
    return requestFactory.build(users);
  }
}
