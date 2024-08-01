package uk.co.mruoc.nac.user.inmemory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.TokenResponse;
import uk.co.mruoc.nac.entities.TokenResponse.TokenResponseBuilder;

@RequiredArgsConstructor
public class StubTokenFactory {

  private final Clock clock;
  private final Supplier<UUID> uuidSupplier;
  private final Algorithm signer;

  public StubTokenFactory(Clock clock, Supplier<UUID> uuidSupplier) {
    this(clock, uuidSupplier, buildSigner());
  }

  public TokenResponse toAccessAndRefreshToken(StubUserTokenConfig config) {
    return toResponseBuilder(config).refreshToken(toRefreshToken(config)).build();
  }

  public TokenResponse toAccessTokenOnly(StubUserTokenConfig config) {
    return toResponseBuilder(config).build();
  }

  private TokenResponseBuilder toResponseBuilder(StubUserTokenConfig config) {
    return TokenResponse.builder()
        .accessToken(toAccessToken(config))
        .username(config.getUsername());
  }

  private String toAccessToken(StubUserTokenConfig config) {
    return toToken(config, Duration.ofMinutes(15));
  }

  private String toRefreshToken(StubUserTokenConfig config) {
    return toToken(config, Duration.ofHours(1));
  }

  private String toToken(StubUserTokenConfig config, Duration validFor) {
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
}
