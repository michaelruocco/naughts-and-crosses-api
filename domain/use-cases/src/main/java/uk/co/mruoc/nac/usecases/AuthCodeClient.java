package uk.co.mruoc.nac.usecases;

import uk.co.mruoc.nac.entities.AccessTokenResponse;
import uk.co.mruoc.nac.entities.AuthCodeRequest;

public interface AuthCodeClient {
  AccessTokenResponse create(AuthCodeRequest request);
}
