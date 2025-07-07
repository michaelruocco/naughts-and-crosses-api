package uk.co.mruoc.nac.usecases;

import uk.co.mruoc.nac.entities.AccessTokenResponse;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.RefreshTokenRequest;

public interface AccessTokenClient {
  AccessTokenResponse create(CreateTokenRequest request);

  AccessTokenResponse refresh(RefreshTokenRequest request);
}
