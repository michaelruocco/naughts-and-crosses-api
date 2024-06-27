package uk.co.mruoc.nac.app;

import java.util.Collection;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiCreateGameRequest;
import uk.co.mruoc.nac.api.dto.ApiCreateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.api.dto.ApiUser;
import uk.co.mruoc.nac.api.factory.ApiCreateGameRequestFactory;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;

@RequiredArgsConstructor
public class Fixtures {

  private final NaughtsAndCrossesApiClient client;
  private final ApiCreateGameRequestFactory requestFactory;

  public Fixtures(NaughtsAndCrossesApiClient client) {
    this(client, new ApiCreateGameRequestFactory());
  }

  public ApiGame givenGameExists() {
    ApiCreateGameRequest request = buildCreateGameRequest();
    return client.createGame(request);
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
    return client.createUser(request);
  }

  public ApiCreateGameRequest buildCreateGameRequest() {
    Collection<ApiUser> users = client.getAllUsers();
    return requestFactory.build(users);
  }
}
