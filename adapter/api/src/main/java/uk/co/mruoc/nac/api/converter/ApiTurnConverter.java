package uk.co.mruoc.nac.api.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiTurn;
import uk.co.mruoc.nac.entities.Turn;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.usecases.AuthenticatedUserSupplier;

@RequiredArgsConstructor
public class ApiTurnConverter {

  private final AuthenticatedUserSupplier userSupplier;
  private final ApiCoordinatesConverter coordinatesConverter;

  public ApiTurnConverter(AuthenticatedUserSupplier userSupplier) {
    this(userSupplier, new ApiCoordinatesConverter());
  }

  public Turn toTurn(ApiTurn apiTurn) {
    User user = userSupplier.get();
    return Turn.builder()
        .coordinates(coordinatesConverter.toCoordinates(apiTurn.getCoordinates()))
        .token(apiTurn.getToken())
        .username(user.getUsername())
        .build();
  }
}
