package uk.co.mruoc.nac.api.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiStatus;
import uk.co.mruoc.nac.entities.Status;

@RequiredArgsConstructor
public class ApiStatusConverter {

  private final ApiStatusPlayerConverter playerConverter;

  public ApiStatusConverter() {
    this(new ApiStatusPlayerConverter());
  }

  public ApiStatus toApiStatus(Status status) {
    return ApiStatus.builder()
        .turn(status.getTurn())
        .complete(status.isComplete())
        .draw(status.isDraw())
        .winner(status.getWinner().map(playerConverter::toApiStatusPlayer).orElse(null))
        .nextPlayer(status.getCurrentPlayer().map(playerConverter::toApiStatusPlayer).orElse(null))
        .build();
  }
}
