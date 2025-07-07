package uk.co.mruoc.nac.user.inmemory;

import java.time.Clock;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.AccessTokenResponse;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.RefreshTokenRequest;
import uk.co.mruoc.nac.usecases.AccessTokenClient;
import uk.co.mruoc.nac.usecases.CreateAccessTokenFailedException;
import uk.co.mruoc.nac.usecases.RefreshAccessTokenFailedException;

@Builder
@RequiredArgsConstructor
public class StubAccessTokenClient implements AccessTokenClient {

  private final StubUserTokenConfigs userConfigs;
  private final StubRefreshTokens refreshTokens;
  private final StubAccessTokenFactory tokenFactory;
  private final JwtValidator validator;

  public StubAccessTokenClient(Clock clock, Supplier<UUID> uuidSupplier, JwtValidator validator) {
    this(
        new StubUserTokenConfigs(),
        new StubRefreshTokens(),
        new StubAccessTokenFactory(clock, uuidSupplier),
        validator);
  }

  @Override
  public AccessTokenResponse create(CreateTokenRequest request) {
    StubUserTokenConfig config = getConfig(request.getUsername());
    if (Objects.equals(config.getPassword(), request.getPassword())) {
      AccessTokenResponse response = tokenFactory.toAccessAndRefreshToken(config);
      refreshTokens.add(response.getRefreshToken(), config);
      return response;
    }
    throw new CreateAccessTokenFailedException(request.getUsername());
  }

  @Override
  public AccessTokenResponse refresh(RefreshTokenRequest request) {
    String refreshToken = request.getRefreshToken();
    validator.validate(refreshToken);
    if (refreshTokens.contains(refreshToken)) {
      StubUserTokenConfig config = refreshTokens.getUserConfig(refreshToken);
      refreshTokens.remove(refreshToken);
      return tokenFactory.toAccessTokenOnly(config);
    }
    throw new RefreshAccessTokenFailedException(refreshToken);
  }

  private StubUserTokenConfig getConfig(String username) {
    return userConfigs
        .getByUsername(username)
        .orElseThrow(() -> new CreateAccessTokenFailedException(username));
  }
}
