package uk.co.mruoc.nac.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class ApiRefreshTokenRequest {

  private final String refreshToken;
}
