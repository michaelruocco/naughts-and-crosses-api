package uk.co.mruoc.nac.repository.postgres.converter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import uk.co.mruoc.nac.entities.Player;
import uk.co.mruoc.nac.entities.Players;
import uk.co.mruoc.nac.repository.postgres.dto.DbPlayer;
import uk.co.mruoc.nac.repository.postgres.dto.DbPlayers;

public class DbPlayerConverter {

    public DbPlayers toDbPlayers(Players players) {
        return DbPlayers.builder()
                .currentIndex(players.getCurrentIndex())
                .values(toDbPlayers(players.stream()))
                .build();
    }

    public Collection<DbPlayer> toDbPlayers(Stream<Player> players) {
        return players.map(this::toDbPlayer).toList();
    }

    public DbPlayer toDbPlayer(Player player) {
        return DbPlayer.builder()
                .name(player.getName())
                .token(player.getToken())
                .build();
    }

    public Players toPlayers(DbPlayers dbPlayers) {
        return Players.builder()
                .currentIndex(dbPlayers.getCurrentIndex())
                .values(toPlayers(dbPlayers.getValues()))
                .build();
    }

    public List<Player> toPlayers(Collection<DbPlayer> players) {
        return players.stream().map(this::toPlayer).toList();
    }

    public Player toPlayer(DbPlayer dbPlayer) {
        return Player.builder()
                .name(dbPlayer.getName())
                .token(dbPlayer.getToken())
                .build();
    }
}
