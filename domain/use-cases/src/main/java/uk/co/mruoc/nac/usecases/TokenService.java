package uk.co.mruoc.nac.usecases;

import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.CreateTokenResponse;

public interface TokenService {
  CreateTokenResponse create(CreateTokenRequest request);
}
