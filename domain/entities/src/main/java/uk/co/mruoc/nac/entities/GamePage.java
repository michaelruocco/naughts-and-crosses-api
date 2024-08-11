package uk.co.mruoc.nac.entities;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GamePage {

  private final long total;
  private final Collection<Game> games;
}
