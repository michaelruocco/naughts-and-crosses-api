package uk.co.mruoc.nac.user.inmemory;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.AuthCodeRequest;
import uk.co.mruoc.nac.entities.TokenResponse;
import uk.co.mruoc.nac.usecases.AuthCodeClient;

@RequiredArgsConstructor
@Builder
public class StubAuthCodeClient implements AuthCodeClient {

  @Override
  public TokenResponse create(AuthCodeRequest request) {
    throw new UnsupportedOperationException("auth codes not supported");
  }
}
