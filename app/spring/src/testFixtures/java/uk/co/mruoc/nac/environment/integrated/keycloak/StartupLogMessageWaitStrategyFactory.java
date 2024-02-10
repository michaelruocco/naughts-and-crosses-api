package uk.co.mruoc.nac.environment.integrated.keycloak;

import java.time.Duration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StartupLogMessageWaitStrategyFactory {

  public static WaitStrategy build() {
    return build(Duration.ofMinutes(3));
  }

  private static WaitStrategy build(Duration duration) {
    return Wait.forLogMessage(".* started in .* Listening on: .*\\n", 1)
        .withStartupTimeout(duration);
  }
}
