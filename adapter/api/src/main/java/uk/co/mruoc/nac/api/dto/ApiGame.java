package uk.co.mruoc.nac.api.dto;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiGame {

    private final long id;
    private final ApiStatus status;
    private final ApiBoard board;
    private final Collection<ApiPlayer> players;
}
