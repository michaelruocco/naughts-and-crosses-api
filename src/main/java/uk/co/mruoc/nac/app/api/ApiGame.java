package uk.co.mruoc.nac.app.api;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiGame {

    private final UUID id;
    private final ApiStatus status;
    private final ApiBoard board;
}
