package uk.co.mruoc.nac.app.api.converter;

import java.util.Collection;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.api.ApiPlayer;
import uk.co.mruoc.nac.app.api.ApiPlayers;
import uk.co.mruoc.nac.app.domain.entities.Player;
import uk.co.mruoc.nac.app.domain.entities.Players;

@RequiredArgsConstructor
public class ApiPlayerConverter {

    public ApiPlayers toApiPlayers(Players players) {
        return ApiPlayers.builder()
                .currentPlayerToken(players.getCurrentPlayerToken())
                .players(toApiPlayers(players.stream()))
                .build();
    }

    public ApiPlayer toApiPlayer(Player player) {
        return ApiPlayer.builder()
                .name(player.getName())
                .token(player.getToken())
                .build();
    }

    private Collection<ApiPlayer> toApiPlayers(Stream<Player> stream) {
        return stream.map(this::toApiPlayer).toList();
    }
}
