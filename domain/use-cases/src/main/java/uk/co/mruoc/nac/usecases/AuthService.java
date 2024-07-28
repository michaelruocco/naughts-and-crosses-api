package uk.co.mruoc.nac.usecases;

import lombok.Builder;
import uk.co.mruoc.nac.entities.AuthCodeRequest;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.RefreshTokenRequest;
import uk.co.mruoc.nac.entities.TokenResponse;

@Builder
public class AuthService {

  private final TokenService tokenService;
  private final AuthCodeClient authCodeClient;

  public TokenResponse create(CreateTokenRequest request) {
    return tokenService.create(request);
  }

  public TokenResponse refresh(RefreshTokenRequest request) {
    return tokenService.refresh(request);
  }

  public TokenResponse create(AuthCodeRequest request) {
    return authCodeClient.create(request);
  }
}
