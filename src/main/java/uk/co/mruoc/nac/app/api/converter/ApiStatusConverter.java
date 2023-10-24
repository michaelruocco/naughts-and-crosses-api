package uk.co.mruoc.nac.app.api.converter;

import uk.co.mruoc.nac.app.api.dto.ApiStatus;
import uk.co.mruoc.nac.app.domain.entities.Status;

public class ApiStatusConverter {

    public ApiStatus toApiStatus(Status status) {
        return ApiStatus.builder()
                .turn(status.getTurn())
                .complete(status.isComplete())
                .nextPlayerToken(status.getCurrentPlayerToken())
                .winningPlayerToken(status.getWinningPlayerToken())
                .build();
    }
}
