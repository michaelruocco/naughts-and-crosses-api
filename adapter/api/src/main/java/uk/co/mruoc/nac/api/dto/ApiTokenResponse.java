package uk.co.mruoc.nac.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiTokenResponse {
  private final String accessToken;
  private final String refreshToken;
}
