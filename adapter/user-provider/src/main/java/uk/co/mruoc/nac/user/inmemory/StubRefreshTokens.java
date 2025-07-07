package uk.co.mruoc.nac.user.inmemory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StubRefreshTokens {

  private final Map<String, StubUserTokenConfig> tokens;

  public StubRefreshTokens() {
    this(new ConcurrentHashMap<>());
  }

  public void add(String token, StubUserTokenConfig config) {
    tokens.put(token, config);
  }

  public boolean contains(String token) {
    return tokens.containsKey(token);
  }

  public StubUserTokenConfig getUserConfig(String token) {
    return tokens.get(token);
  }

  public void remove(String token) {
    tokens.remove(token);
  }
}
