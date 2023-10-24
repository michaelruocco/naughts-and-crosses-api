package uk.co.mruoc.nac.app.api.converter;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.app.api.dto.ApiPlayer;
import uk.co.mruoc.nac.app.domain.entities.Player;
import uk.co.mruoc.nac.app.domain.entities.Players;

@RequiredArgsConstructor
public class ApiPlayerConverter {

    public Collection<ApiPlayer> toApiPlayers(Players players) {
        return players.stream().map(this::toApiPlayer).toList();
    }

    public ApiPlayer toApiPlayer(Player player) {
        return ApiPlayer.builder()
                .name(player.getName())
                .token(player.getToken())
                .build();
    }
}
