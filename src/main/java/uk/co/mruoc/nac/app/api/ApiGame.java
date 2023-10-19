package uk.co.mruoc.nac.app.api;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiGame {

    private final UUID id;
    private final ApiStatus status;
    private final Collection<ApiPlayer> players;
    private final ApiBoard board;
}
