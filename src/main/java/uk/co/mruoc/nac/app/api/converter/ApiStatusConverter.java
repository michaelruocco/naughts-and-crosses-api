package uk.co.mruoc.nac.app.api.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.api.ApiStatus;
import uk.co.mruoc.nac.app.domain.entities.Status;

@RequiredArgsConstructor
public class ApiStatusConverter {

    private final ApiPlayerConverter playerConverter;

    public ApiStatusConverter() {
        this(new ApiPlayerConverter());
    }

    public ApiStatus toApiStatus(Status status) {
        return ApiStatus.builder()
                .turn(status.getTurn())
                .complete(status.isComplete())
                .nextPlayerToken(status.getCurrentPlayerToken())
                .players(playerConverter.toApiPlayers(status.getPlayers()))
                .build();
    }
}
