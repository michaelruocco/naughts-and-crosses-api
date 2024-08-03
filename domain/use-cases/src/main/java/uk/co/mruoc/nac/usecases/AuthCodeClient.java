package uk.co.mruoc.nac.usecases;

import uk.co.mruoc.nac.entities.AuthCodeRequest;
import uk.co.mruoc.nac.entities.TokenResponse;

public interface AuthCodeClient {
  TokenResponse create(AuthCodeRequest request);
}
