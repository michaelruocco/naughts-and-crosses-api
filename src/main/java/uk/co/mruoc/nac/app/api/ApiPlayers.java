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
public class ApiPlayers {

    private final char currentPlayerToken;
    private final Collection<ApiPlayer> players;
}
