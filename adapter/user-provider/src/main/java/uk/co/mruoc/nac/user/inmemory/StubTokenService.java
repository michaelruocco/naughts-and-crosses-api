package uk.co.mruoc.nac.user.inmemory;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.RefreshTokenRequest;
import uk.co.mruoc.nac.entities.TokenResponse;
import uk.co.mruoc.nac.usecases.CreateTokenFailedException;
import uk.co.mruoc.nac.usecases.RefreshTokenFailedException;
import uk.co.mruoc.nac.usecases.TokenService;
import uk.co.mruoc.nac.user.JwtParser;

@RequiredArgsConstructor
public class StubTokenService implements TokenService {

  private final StubTokenFactory tokenFactory;
  private final JwtValidator validator;
  private final StubUserTokenConfigs userConfigs;
  private final Map<String, StubUserTokenConfig> refreshTokens;

  public StubTokenService(Clock clock, Supplier<UUID> uuidSupplier, JwtParser jwtParser) {
    this(clock, uuidSupplier, new JwtValidator(clock, jwtParser));
  }

  public StubTokenService(Clock clock, Supplier<UUID> uuidSupplier, JwtValidator validator) {
    this(
        new StubTokenFactory(clock, uuidSupplier),
        validator,
        new StubUserTokenConfigs(),
        new HashMap<>());
  }

  @Override
  public TokenResponse create(CreateTokenRequest request) {
    StubUserTokenConfig config = getConfig(request.getUsername());
    if (Objects.equals(config.getPassword(), request.getPassword())) {
      TokenResponse response = tokenFactory.toAccessAndRefreshToken(config);
      refreshTokens.put(response.getRefreshToken(), config);
      return response;
    }
    throw new CreateTokenFailedException(request.getUsername());
  }

  @Override
  public TokenResponse refresh(RefreshTokenRequest request) {
    String refreshToken = request.getRefreshToken();
    validator.validate(refreshToken);
    if (refreshTokens.containsKey(refreshToken)) {
      StubUserTokenConfig config = refreshTokens.get(refreshToken);
      refreshTokens.remove(refreshToken);
      return tokenFactory.toAccessTokenOnly(config);
    }
    throw new RefreshTokenFailedException(refreshToken);
  }

  private StubUserTokenConfig getConfig(String username) {
    return userConfigs
        .getByUsername(username)
        .orElseThrow(() -> new CreateTokenFailedException(username));
  }
}
