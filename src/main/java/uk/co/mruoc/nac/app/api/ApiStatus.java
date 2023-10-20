package uk.co.mruoc.nac.app.api;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiStatus {

    private final int turn;
    private final boolean complete;
    private final char nextPlayerToken;
    private final Collection<ApiPlayer> players;
}
