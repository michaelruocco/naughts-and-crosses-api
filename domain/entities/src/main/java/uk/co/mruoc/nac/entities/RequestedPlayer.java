package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Data
public class RequestedPlayer {

  private final String userId;
  private final char token;
}
