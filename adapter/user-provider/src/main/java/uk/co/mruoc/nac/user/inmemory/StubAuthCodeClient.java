package uk.co.mruoc.nac.user.inmemory;

import java.time.Clock;
import java.util.UUID;
import java.util.function.Supplier;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.AccessTokenResponse;
import uk.co.mruoc.nac.entities.AuthCodeRequest;
import uk.co.mruoc.nac.usecases.AuthCodeClient;
import uk.co.mruoc.nac.usecases.CreateAccessTokenFailedException;

@RequiredArgsConstructor
@Builder
public class StubAuthCodeClient implements AuthCodeClient {

  private final StubUserTokenConfigs userConfigs;
  private final StubAccessTokenFactory tokenFactory;

  public StubAuthCodeClient(Clock clock, Supplier<UUID> uuidSupplier) {
    this(new StubUserTokenConfigs(), new StubAccessTokenFactory(clock, uuidSupplier));
  }

  @Override
  public AccessTokenResponse create(AuthCodeRequest request) {
    StubUserTokenConfig config = getConfig(request.getAuthCode());
    return tokenFactory.toAccessAndRefreshToken(config);
  }

  private StubUserTokenConfig getConfig(String authCode) {
    return userConfigs
        .getByAuthCode(authCode)
        .orElseThrow(() -> new CreateAccessTokenFailedException(authCode));
  }
}
