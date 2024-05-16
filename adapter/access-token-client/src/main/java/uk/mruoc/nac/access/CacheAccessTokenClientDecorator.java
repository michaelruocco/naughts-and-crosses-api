package uk.mruoc.nac.access;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class CacheAccessTokenClientDecorator implements AccessTokenClient {

  private final AccessTokenClient client;
  private final Clock clock;
  private final Duration cutoffBuffer;

  private final Map<String, AccessToken> cachedTokens;

  public CacheAccessTokenClientDecorator(AccessTokenClient client) {
    this(client, Clock.systemUTC(), Duration.ofSeconds(30), new HashMap<>());
  }

  @Override
  public AccessToken generateToken(TokenCredentials credentials) {
    String username = credentials.getUsername();
    if (isNewTokenRequired(username)) {
      AccessToken token = client.generateToken(credentials);
      cachedTokens.put(username, token);
    }
    return cachedTokens.get(username);
  }

  private boolean isNewTokenRequired(String username) {
    Optional<AccessToken> cachedToken = getCachedToken(username);
    return cachedToken.map(accessToken -> !isExpired(accessToken)).orElse(true);
  }

  private Optional<AccessToken> getCachedToken(String username) {
    return Optional.ofNullable(cachedTokens.get(username));
  }

  private boolean isExpired(AccessToken token) {
    Instant cutoff = clock.instant().plus(cutoffBuffer);
    boolean expired = token.isExpired(cutoff);
    log.debug("token expired {}", expired);
    return expired;
  }
}
