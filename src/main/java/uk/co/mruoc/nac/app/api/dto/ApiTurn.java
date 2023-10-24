package uk.co.mruoc.nac.app.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiTurn {

    private final ApiCoordinates coordinates;
    private final char token;
}
