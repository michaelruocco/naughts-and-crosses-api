package uk.co.mruoc.nac.user.inmemory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.CreateTokenResponse;
import uk.co.mruoc.nac.usecases.CreateTokenFailedException;
import uk.co.mruoc.nac.usecases.TokenService;

@RequiredArgsConstructor
public class StubTokenService implements TokenService {

  private final Clock clock;
  private final Algorithm signer;
  private final Map<String, StubTokenConfig> userConfig;

  public StubTokenService(Clock clock) {
    this(clock, buildSigner());
  }

  public StubTokenService(Clock clock, Algorithm signer) {
    this(clock, signer, buildUserConfig());
  }

  @Override
  public CreateTokenResponse create(CreateTokenRequest request) {
    StubTokenConfig config = getConfig(request.getUsername());
    if (Objects.equals(config.getPassword(), request.getPassword())) {
      return toResponse(config);
    }
    throw new CreateTokenFailedException(request.getUsername());
  }

  private StubTokenConfig getConfig(String username) {
    return Optional.of(userConfig.get(username))
        .orElseThrow(() -> new CreateTokenFailedException(username));
  }

  private CreateTokenResponse toResponse(StubTokenConfig config) {
    Instant now = clock.instant();
    Instant expiry = now.plus(Duration.ofHours(1));
    String token =
        JWT.create()
            .withSubject(config.getSubject())
            .withIssuer("nac-stub-token-service")
            .withIssuedAt(now)
            .withExpiresAt(expiry)
            .sign(signer);
    return CreateTokenResponse.builder().accessToken(token).expiry(expiry).build();
  }

  private static Algorithm buildSigner() {
    return new AlgorithmSupplier().get();
  }

  private static Map<String, StubTokenConfig> buildUserConfig() {
    StubTokenConfig user1 =
        StubTokenConfig.builder()
            .username("user-1")
            .password("pwd1")
            .subject("707d9fa6-13dd-4985-93aa-a28f01e89a6b")
            .build();
    StubTokenConfig user2 =
        StubTokenConfig.builder()
            .username("user-2")
            .password("pwd2")
            .subject("dadfde25-9924-4982-802d-dfd0bce2218d")
            .build();
    return Map.of(user1.getUsername(), user1, user2.getUsername(), user2);
  }
}
