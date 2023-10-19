package uk.co.mruoc.nac.app.api;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiStatus {

    private final boolean complete;
    private final ApiPlayer winner;
    private final ApiPlayer nextPlayer;

    public boolean isNextPlayer(char token) {
        return Optional.ofNullable(nextPlayer)
                .map(np -> np.getToken() == token)
                .orElse(false);
    }
}
