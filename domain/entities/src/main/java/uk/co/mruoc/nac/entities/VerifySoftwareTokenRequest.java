package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.With;

@RequiredArgsConstructor
@Builder
@Data
public class VerifySoftwareTokenRequest {
  private final String userCode;
  @With private final String accessToken;
}
