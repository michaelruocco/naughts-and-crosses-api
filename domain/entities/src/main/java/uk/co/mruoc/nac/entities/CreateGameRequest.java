package uk.co.mruoc.nac.entities;

import java.util.Collection;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class CreateGameRequest {

  private final Collection<RequestedPlayer> requestedPlayers;
}
