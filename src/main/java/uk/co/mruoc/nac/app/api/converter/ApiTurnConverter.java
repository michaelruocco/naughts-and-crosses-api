package uk.co.mruoc.nac.app.api.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.api.ApiTurn;
import uk.co.mruoc.nac.app.domain.entities.Turn;

@RequiredArgsConstructor
public class ApiTurnConverter {

    private final ApiCoordinatesConverter coordinatesConverter;

    public ApiTurnConverter() {
        this(new ApiCoordinatesConverter());
    }

    public Turn toTurn(ApiTurn apiTurn) {
        return Turn.builder()
                .coordinates(coordinatesConverter.toCoordinates(apiTurn.getCoordinates()))
                .token(apiTurn.getToken())
                .build();
    }
}
