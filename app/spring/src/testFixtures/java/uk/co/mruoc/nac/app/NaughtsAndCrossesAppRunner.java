package uk.co.mruoc.nac.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;

import static org.awaitility.Awaitility.await;

@Slf4j
public class NaughtsAndCrossesAppRunner {

    private int port;
    private ConfigurableApplicationContext context;

    public void startIfNotStarted() {
        if (isRunning()) {
            log.info("app already running so not starting");
            return;
        }
        port = AvailablePortFinder.findAvailableTcpPort();
        String[] args = toArgs(port);
        log.info("starting naughts and crosses application with args {}", Arrays.toString(args));
        context = SpringApplication.run(NaughtsAndCrossesApp.class, args);
        log.info("waiting for app startup to complete...");
        waitForStartupToComplete();
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

    public int getPort() {
        return port;
    }

    private boolean isRunning() {
        return Objects.nonNull(context);
    }

    private void waitForStartupToComplete() {
        await().dontCatchUncaughtExceptions()
                .atMost(Duration.ofSeconds(5))
                .pollInterval(Duration.ofMillis(250))
                .until(PortReady.local(port));
    }

    private static String[] toArgs(int port) {
        return new String[] {
                String.format("--server.port=%d", port),
        };
    }
}
