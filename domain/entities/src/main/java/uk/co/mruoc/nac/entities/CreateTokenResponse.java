package uk.co.mruoc.nac.entities;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Data
public class CreateTokenResponse {
  private final String accessToken;
  private final Instant expiry;
}
