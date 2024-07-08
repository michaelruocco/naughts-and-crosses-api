package uk.co.mruoc.nac.user.inmemory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.RefreshTokenRequest;
import uk.co.mruoc.nac.entities.TokenResponse;
import uk.co.mruoc.nac.usecases.CreateTokenFailedException;
import uk.co.mruoc.nac.usecases.RefreshTokenFailedException;
import uk.co.mruoc.nac.usecases.TokenService;

@RequiredArgsConstructor
public class StubTokenService implements TokenService {

  private final Clock clock;
  private final Supplier<UUID> uuidSupplier;
  private final JwtValidator validator;
  private final Algorithm signer;
  private final Map<String, StubTokenConfig> userConfig;
  private final Map<String, StubTokenConfig> refreshTokens;

  public StubTokenService(Clock clock, Supplier<UUID> uuidSupplier, ObjectMapper mapper) {
    this(clock, uuidSupplier, new JwtValidator(clock, mapper), buildSigner());
  }

  public StubTokenService(
      Clock clock, Supplier<UUID> uuidSupplier, JwtValidator validator, Algorithm signer) {
    this(clock, uuidSupplier, validator, signer, buildUserConfig(), new HashMap<>());
  }

  @Override
  public TokenResponse create(CreateTokenRequest request) {
    StubTokenConfig config = getConfig(request.getUsername());
    if (Objects.equals(config.getPassword(), request.getPassword())) {
      TokenResponse response =
          toResponseBuilder(config).refreshToken(toRefreshToken(config)).build();
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
      StubTokenConfig config = refreshTokens.get(refreshToken);
      refreshTokens.remove(refreshToken);
      return toResponseBuilder(config).build();
    }
    throw new RefreshTokenFailedException(refreshToken);
  }

  private StubTokenConfig getConfig(String username) {
    return Optional.of(userConfig.get(username))
        .orElseThrow(() -> new CreateTokenFailedException(username));
  }

  private TokenResponse.TokenResponseBuilder toResponseBuilder(StubTokenConfig config) {
    return TokenResponse.builder().accessToken(toAccessToken(config));
  }

  private String toAccessToken(StubTokenConfig config) {
    return toToken(config, Duration.ofMinutes(15));
  }

  private String toRefreshToken(StubTokenConfig config) {
    return toToken(config, Duration.ofHours(1));
  }

  private String toToken(StubTokenConfig config, Duration validFor) {
    Instant now = clock.instant();
    return JWT.create()
        .withSubject(config.getSubject())
        .withIssuer("nac-stub-token-service")
        .withClaim("username", config.getUsername())
        .withClaim("uniqueId", uuidSupplier.get().toString())
        .withIssuedAt(now)
        .withExpiresAt(now.plus(validFor))
        .sign(signer);
  }

  private static Algorithm buildSigner() {
    return new AlgorithmSupplier().get();
  }

  private static Map<String, StubTokenConfig> buildUserConfig() {
    return toMap(List.of(admin(), user1(), user2()));
  }

  private static StubTokenConfig admin() {
    return StubTokenConfig.builder()
        .username("admin")
        .password("pwd")
        .subject("42bfdad9-c66a-4d5a-aa48-a97d6d3574af")
        .build();
  }

  private static StubTokenConfig user1() {
    return StubTokenConfig.builder()
        .username("user-1")
        .password("pwd1")
        .subject("707d9fa6-13dd-4985-93aa-a28f01e89a6b")
        .build();
  }

  private static StubTokenConfig user2() {
    return StubTokenConfig.builder()
        .username("user-2")
        .password("pwd2")
        .subject("dadfde25-9924-4982-802d-dfd0bce2218d")
        .build();
  }

  private static Map<String, StubTokenConfig> toMap(Collection<StubTokenConfig> configs) {
    return configs.stream()
        .collect(Collectors.toMap(StubTokenConfig::getUsername, Function.identity()));
  }
}
