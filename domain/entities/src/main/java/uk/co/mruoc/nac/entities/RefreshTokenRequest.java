package uk.co.mruoc.nac.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class RefreshTokenRequest {
  private final String refreshToken;
}
