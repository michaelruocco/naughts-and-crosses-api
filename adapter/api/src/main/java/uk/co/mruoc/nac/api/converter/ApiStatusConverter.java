package uk.co.mruoc.nac.api.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiStatus;
import uk.co.mruoc.nac.entities.Status;

@RequiredArgsConstructor
public class ApiStatusConverter {

  public ApiStatus toApiStatus(Status status) {
    return ApiStatus.builder()
        .turn(status.getTurn())
        .complete(status.isComplete())
        .draw(status.isDraw())
        .winner(status.getWinner().orElse(null))
        .nextPlayerToken(status.getCurrentPlayerToken().orElse(null))
        .build();
  }
}
