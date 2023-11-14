package uk.co.mruoc.nac.api.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiBoard;
import uk.co.mruoc.nac.entities.Board;

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

  public Board toBoard(ApiBoard apiBoard) {
    return Board.builder()
        .size(apiBoard.getSize())
        .locations(locationConverter.toLocationsMap(apiBoard.getLocations()))
        .build();
  }
}
