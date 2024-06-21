package uk.co.mruoc.nac.api.dto;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiCreateTokenResponse {
  private final String accessToken;
  private final Instant expiry;
}
