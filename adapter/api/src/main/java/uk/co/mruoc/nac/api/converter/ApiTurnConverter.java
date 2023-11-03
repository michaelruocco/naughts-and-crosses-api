package uk.co.mruoc.nac.api.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiTurn;
import uk.co.mruoc.nac.entities.Turn;

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
