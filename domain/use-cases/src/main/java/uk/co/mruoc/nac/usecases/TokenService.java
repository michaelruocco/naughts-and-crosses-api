package uk.co.mruoc.nac.usecases;

import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.RefreshTokenRequest;
import uk.co.mruoc.nac.entities.TokenResponse;

public interface TokenService {
  TokenResponse create(CreateTokenRequest request);

  TokenResponse refresh(RefreshTokenRequest request);
}
