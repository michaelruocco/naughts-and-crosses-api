package uk.co.mruoc.nac.api.converter;

import uk.co.mruoc.nac.api.dto.ApiStatus;
import uk.co.mruoc.nac.entities.Status;

public class ApiStatusConverter {

  public ApiStatus toApiStatus(Status status) {
    return ApiStatus.builder()
        .turn(status.getTurn())
        .complete(status.isComplete())
        .nextPlayerToken(status.getCurrentPlayerToken().orElse(null))
        .build();
  }
}
