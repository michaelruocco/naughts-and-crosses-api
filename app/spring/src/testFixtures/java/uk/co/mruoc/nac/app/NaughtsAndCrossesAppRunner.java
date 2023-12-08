package uk.co.mruoc.nac.app;

import static org.awaitility.Awaitility.await;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class NaughtsAndCrossesAppRunner {

  private ConfigurableApplicationContext context;

  public void startIfNotStarted(TestEnvironment environment) {
    if (isRunning()) {
      log.info("app already running so not starting");
      return;
    }
    String[] args = environment.getAppArgs();
    log.info("starting naughts and crosses application with args {}", Arrays.toString(args));
    context = SpringApplication.run(NaughtsAndCrossesApp.class, args);
    log.info("waiting for app startup to complete...");
    waitForStartupToComplete(environment.getAppPort());
  }

  public void shutdownIfRunning() {
    if (!isRunning()) {
      log.info("app not running so cannot shut down");
      return;
    }
    log.info("shutting down app");
    SpringApplication.exit(context, () -> 0);
    context = null;
  }

  private boolean isRunning() {
    return Objects.nonNull(context);
  }

  private void waitForStartupToComplete(int port) {
    await()
        .dontCatchUncaughtExceptions()
        .atMost(Duration.ofSeconds(5))
        .pollInterval(Duration.ofMillis(250))
        .until(PortReady.local(port));
  }
}
