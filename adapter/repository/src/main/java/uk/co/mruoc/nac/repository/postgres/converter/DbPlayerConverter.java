package uk.co.mruoc.nac.repository.postgres.converter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.Player;
import uk.co.mruoc.nac.entities.Players;
import uk.co.mruoc.nac.repository.postgres.dto.DbPlayer;
import uk.co.mruoc.nac.repository.postgres.dto.DbPlayers;

@RequiredArgsConstructor
public class DbPlayerConverter {

  private final DbUserConverter userConverter;

  public DbPlayerConverter() {
    this(new DbUserConverter());
  }

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
        .user(userConverter.toDbUser(player.getUser()))
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
        .user(userConverter.toUser(dbPlayer.getUser()))
        .token(dbPlayer.getToken())
        .build();
  }
}
