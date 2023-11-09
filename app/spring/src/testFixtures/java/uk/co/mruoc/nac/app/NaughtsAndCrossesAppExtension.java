package uk.co.mruoc.nac.app;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import uk.co.mruoc.nac.client.NaughtsAndCrossesApiClient;
import uk.co.mruoc.nac.postgres.TestPostgresContainer;

@Slf4j
public class NaughtsAndCrossesAppExtension
        implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback, CloseableResource {

    private boolean started = false;

    private static final TestPostgresContainer POSTGRES = new TestPostgresContainer();

    private static final NaughtsAndCrossesAppRunner APP_RUNNER = new NaughtsAndCrossesAppRunner();

    private static final AppConfig APP_CONFIG = AppConfig.builder()
            .appPort(AvailablePortFinder.findAvailableTcpPort())
            .dbHost(POSTGRES.getHost())
            .dbName(POSTGRES.getDatabaseName())
            .dbPort(POSTGRES::getFirstMappedPort)
            .build();

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        if (!started) {
            log.info("starting extension");
            log.info("starting postgres");
            POSTGRES.start();
            log.info("starting kafka");
            APP_RUNNER.startIfNotStarted(APP_CONFIG);
            log.info("extension startup complete");
            started = true;
        }
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        NaughtsAndCrossesApiClient client = getAppClient();
        client.deleteAllGames();
        client.resetIds();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        shutdown();
    }

    @Override
    public void close() {
        shutdown();
    }

    public NaughtsAndCrossesApiClient getAppClient() {
        return new NaughtsAndCrossesApiClient(APP_CONFIG.getAppUrl());
    }

    private void shutdown() {
        if (started) {
            APP_RUNNER.shutdownIfRunning();
            POSTGRES.close();
        }
    }
}
