package uk.co.mruoc.nac.api.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import uk.co.mruoc.nac.api.dto.ApiCreateGameRequest;
import uk.co.mruoc.nac.api.dto.ApiRequestedPlayer;
import uk.co.mruoc.nac.api.dto.ApiUser;

public class ApiCreateGameRequestFactory {

  public ApiCreateGameRequest build(Collection<ApiUser> users) {
    if (users.isEmpty()) {
      throw new AtLeastOneUserRequiredException();
    }
    List<ApiUser> userList = new ArrayList<>(users);
    ApiUser user1 = userList.get(0);
    if (userList.size() == 1) {
      return build(user1);
    }
    return build(user1, userList.get(1));
  }

  public ApiCreateGameRequest build(ApiUser user) {
    return build(user, user);
  }

  public ApiCreateGameRequest build(ApiUser user1, ApiUser user2) {
    return ApiCreateGameRequest.builder()
        .requestedPlayers(List.of(toRequestedPlayer(user1, 'X'), toRequestedPlayer(user2, 'O')))
        .build();
  }

  private static ApiRequestedPlayer toRequestedPlayer(ApiUser user, char token) {
    return ApiRequestedPlayer.builder().userId(user.getId()).token(token).build();
  }
}
