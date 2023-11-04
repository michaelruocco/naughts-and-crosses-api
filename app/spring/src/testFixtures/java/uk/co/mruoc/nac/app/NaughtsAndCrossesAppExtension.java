package uk.co.mruoc.nac.app;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

public class NaughtsAndCrossesAppExtension implements BeforeAllCallback, AfterAllCallback, CloseableResource {

    private static final NaughtsAndCrossesAppRunner APP_RUNNER = new NaughtsAndCrossesAppRunner();

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        APP_RUNNER.startIfNotStarted();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        shutdown();
    }

    @Override
    public void close() {
        shutdown();
    }

    public String getAppBaseUrl() {
        return String.format("http://localhost:%d", APP_RUNNER.getPort());
    }

    private void shutdown() {
        APP_RUNNER.shutdownIfRunning();
    }
}
