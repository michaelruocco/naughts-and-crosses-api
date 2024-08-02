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
  private final RetryConfig config;
  private final String name;

  public ExternalUserPresentRetry(ExternalUserService service) {
    this(service, 5, Duration.ofMillis(200));
  }

  public ExternalUserPresentRetry(
      ExternalUserService service, int maxAttempts, Duration waitDuration) {
    this(service, buildRetryConfig(maxAttempts, waitDuration), "auth-code-external-user");
  }

  public void waitUntilExternalUserPresent(String username) {
    Retry.of(name, config).executeRunnable(() -> tryGetExternalUser(username));
  }

  private void tryGetExternalUser(String username) {
    Optional<User> user = service.getByUsername(username);
    log.info("performed lookup of external user {} present {}", username, user);
    if (user.isEmpty()) {
      throw new UserNotFoundException(username);
    }
  }

  private static RetryConfig buildRetryConfig(int maxAttempts, Duration waitDuration) {
    return RetryConfig.custom()
        .retryExceptions(UserNotFoundException.class)
        .maxAttempts(maxAttempts)
        .waitDuration(waitDuration)
        .failAfterMaxAttempts(true)
        .build();
  }
}
