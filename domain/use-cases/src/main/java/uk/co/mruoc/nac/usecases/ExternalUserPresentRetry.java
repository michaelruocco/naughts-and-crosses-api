package uk.co.mruoc.nac.usecases;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.User;

@RequiredArgsConstructor
@Slf4j
public class ExternalUserPresentRetry {

  private final ExternalUserService service;
  private final String name;
  private final RetryConfig config;

  public ExternalUserPresentRetry(ExternalUserService service) {
    this(service, "auth-code-external-user", buildDefaultConfig());
  }

  public void waitUntilExternalUserPresent(String username) {
    Retry.of(name, config).executeRunnable(() -> tryGetExternalUser(username));
  }

  private void tryGetExternalUser(String username) {
    Optional<User> user = service.getByUsername(username);
    log.info("performed lookup of external user {} present {}", username, user.isPresent());
    if (user.isEmpty()) {
      throw new UserNotFoundException(username);
    }
  }

  private static RetryConfig buildDefaultConfig() {
    return RetryConfig.custom()
        .retryExceptions(UserNotFoundException.class)
        .maxAttempts(5)
        .waitDuration(Duration.ofMillis(200))
        .failAfterMaxAttempts(true)
        .build();
  }
}
