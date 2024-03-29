package uk.co.mruoc.nac.app;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiCreateGameRequest;
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

  public ApiCreateGameRequest buildCreateGameRequest() {
    Collection<ApiUser> users = client.getAllUsers();
    return requestFactory.build(users);
  }
}
