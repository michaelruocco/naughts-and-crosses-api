package uk.co.mruoc.nac.app.api.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.api.dto.ApiBoard;
import uk.co.mruoc.nac.app.domain.entities.Board;

@RequiredArgsConstructor
public class ApiBoardConverter {

    private final ApiLocationConverter locationConverter;

    public ApiBoardConverter() {
        this(new ApiLocationConverter());
    }

    public ApiBoard toApiBoard(Board board) {
        return ApiBoard.builder()
                .size(board.getSize())
                .locations(locationConverter.toApiLocations(board.getLocations()))
                .build();
    }
}
